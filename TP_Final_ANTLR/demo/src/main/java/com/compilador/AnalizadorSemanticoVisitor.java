package com.compilador;

import java.util.*;

public class AnalizadorSemanticoVisitor extends MiLenguajeBaseVisitor<Void> {
    private final TablaSimbolos tablaSimbolos = new TablaSimbolos();
    private final List<String> erroresSemanticos = new ArrayList<>();
    // Cambiar a TreeMap para mantener orden por línea automáticamente
    private final TreeMap<Integer, List<String>> warningsSemanticosPorLinea = new TreeMap<>();

    // Conjuntos para rastrear el ciclo de vida de las variables
    private final Set<String> variablesDeclaradas = new HashSet<>();
    private final Set<String> variablesUsadas = new HashSet<>();
    private final Set<String> variablesInicializadas = new HashSet<>();

    // Mapa para almacenar las líneas donde se declaran las variables
    private final Map<String, Integer> lineasDeclaracion = new HashMap<>();

    // Para verificar retornos de funciones
    private String tipoRetornoFuncionActual = null;
    private boolean funcionTieneReturn = false;

    // Para detectar código inalcanzable
    private boolean despuesDeReturn = false;

    public List<String> getErroresSemanticos() {
        return erroresSemanticos;
    }

    public List<String> getWarningsSemanticos() {
        // Aplanar el TreeMap en una lista ordenada por línea
        List<String> warningsOrdenados = new ArrayList<>();
        for (List<String> warningsLinea : warningsSemanticosPorLinea.values()) {
            warningsOrdenados.addAll(warningsLinea);
        }
        return warningsOrdenados;
    }

    // Método auxiliar para agregar warnings manteniendo el orden por línea
    private void agregarWarning(String warning, int linea) {
        warningsSemanticosPorLinea.computeIfAbsent(linea, k -> new ArrayList<>()).add(warning);
    }

    /**
     * Verifica variables no usadas al final del análisis (solo para scope global)
     */
    public void verificarVariablesNoUsadas() {
        // Solo verificar variables globales, las locales se verifican en cada función
        for (String variable : variablesDeclaradas) {
            if (!variablesUsadas.contains(variable)) {
                Integer linea = lineasDeclaracion.get(variable);
                if (linea != null) {
                    agregarWarning(
                            "Warning: Variable '" + variable + "' declarada pero nunca usada (línea " + linea + ")",
                            linea);
                }
            }
        }
    }

    /**
     * Visita declaraciones de variables, registra en tabla de símbolos y verifica
     * tipos
     */
    @Override
    public Void visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();

        for (MiLenguajeParser.DeclaradorContext declarador : ctx.listaDeclaracion().declarador()) {
            String nombre = declarador.ID().getText();
            int linea = declarador.ID().getSymbol().getLine();
            int columna = declarador.ID().getSymbol().getCharPositionInLine();

            if (!tablaSimbolos.declarar(nombre, tipo, linea, columna)) {
                erroresSemanticos
                        .add("Error: Variable '" + nombre + "' ya declarada en este ámbito (línea " + linea + ")");
            }

            variablesDeclaradas.add(nombre);
            lineasDeclaracion.put(nombre, linea);

            // NUEVO: Verificar compatibilidad de tipos en la inicialización
            if (declarador.expresion() != null) {
                visit(declarador.expresion());
                variablesInicializadas.add(nombre);

                // Verificar tipo de la expresión de inicialización
                String tipoInicializacion = obtenerTipoExpresion(declarador.expresion());

                if (tipoInicializacion != null && !tipoInicializacion.equals(tipo)) {
                    if (esConversionSegura(tipoInicializacion, tipo)) {
                        agregarWarning("Warning: Conversión implícita de " + tipoInicializacion + " a " + tipo +
                                " en inicialización de '" + nombre + "' (línea " + linea + ")", linea);
                    } else {
                        erroresSemanticos.add("Error: Tipos incompatibles en inicialización. No se puede convertir " +
                                tipoInicializacion + " a " + tipo + " para variable '" + nombre +
                                "' (línea " + linea + ")");
                    }
                }
            }
        }

        return null;
    }

    /**
     * Visita asignaciones, verifica que la variable exista y los tipos sean
     * compatibles
     */
    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String nombreVariable = ctx.ID().getText();

        // Verificar si la variable existe
        if (!tablaSimbolos.existe(nombreVariable)) {
            int linea = ctx.ID().getSymbol().getLine();
            erroresSemanticos.add("Error: Variable '" + nombreVariable + "' no declarada (línea " + linea + ")");
            return null;
        }

        // Marcar como inicializada después de asignación
        variablesInicializadas.add(nombreVariable);

        // Verificar tipo de la expresión
        if (ctx.expresion() != null) {
            visit(ctx.expresion());

            // Verificar compatibilidad de tipos
            String tipoVariable = tablaSimbolos.obtenerTipo(nombreVariable);
            String tipoExpresion = obtenerTipoExpresion(ctx.expresion());

            if (tipoVariable != null && tipoExpresion != null && !tipoVariable.equals(tipoExpresion)) {
                if (esConversionSegura(tipoExpresion, tipoVariable)) {
                    agregarWarning("Warning: Conversión implícita de " + tipoExpresion + " a " + tipoVariable +
                            " (línea " + ctx.start.getLine() + ")", ctx.start.getLine());
                } else {
                    erroresSemanticos.add("Error: Tipos incompatibles en asignación. No se puede convertir " +
                            tipoExpresion + " a " + tipoVariable + " (línea " + ctx.start.getLine() + ")");
                }
            }
        }

        return null;
    }

    /**
     * Visita uso de identificadores en expresiones, verifica que estén declarados
     */
    @Override
    public Void visitIdExpr(MiLenguajeParser.IdExprContext ctx) {
        String nombreVariable = ctx.ID().getText();

        // Verificar si la variable existe en cualquier ámbito
        if (!tablaSimbolos.existe(nombreVariable)) {
            int linea = ctx.ID().getSymbol().getLine();
            erroresSemanticos.add("Error: Variable '" + nombreVariable + "' no declarada (línea " + linea + ")");
        } else {
            // Warning: uso de variable potencialmente no inicializada
            if (!variablesInicializadas.contains(nombreVariable)) {
                int linea = ctx.ID().getSymbol().getLine();
                agregarWarning("Warning: Variable '" + nombreVariable + "' usada sin inicializar (línea " + linea + ")",
                        linea);
            }
        }

        variablesUsadas.add(nombreVariable);
        return null;
    }

    /**
     * Visita operaciones de multiplicación y división, detecta divisiones por cero
     */
    @Override
    public Void visitMulDivExpr(MiLenguajeParser.MulDivExprContext ctx) {
        // Verificar división por cero
        if (ctx.op.getText().equals("/")) {
            String textoDerecho = ctx.expresion(1).getText();
            if (textoDerecho.equals("0")) {
                agregarWarning("Warning: División por cero detectada (línea " + ctx.start.getLine() + ")",
                        ctx.start.getLine());
            }
        }
        return visitChildren(ctx);
    }

    /**
     * Maneja scopes de funciones y registra la función en la tabla de símbolos
     */
    @Override
    public Void visitFuncion(MiLenguajeParser.FuncionContext ctx) {
        String nombreFuncion = ctx.ID().getText();
        String tipoRetorno = ctx.tipo().getText();
        int linea = ctx.ID().getSymbol().getLine();
        int columna = ctx.ID().getSymbol().getCharPositionInLine();

        StringBuilder parametrosStr = new StringBuilder();
        List<String> tiposParametros = new ArrayList<>();
        Set<String> nombresParametros = new HashSet<>();

        // Procesar parámetros
        if (ctx.parametros() != null) {
            for (int i = 0; i < ctx.parametros().parametro().size(); i++) {
                if (i > 0)
                    parametrosStr.append(", ");
                MiLenguajeParser.ParametroContext param = ctx.parametros().parametro(i);
                String tipoParam = param.tipo().getText();
                String nombreParam = param.ID().getText();

                // ERROR: Verificar parámetros duplicados - pero NO impedir su registro
                if (nombresParametros.contains(nombreParam)) {
                    erroresSemanticos.add("Error: Parámetro '" + nombreParam + "' duplicado en función '" +
                            nombreFuncion + "' (línea " + param.start.getLine() + ")");
                }
                // SIEMPRE agregar el parámetro, incluso si está duplicado
                nombresParametros.add(nombreParam);

                parametrosStr.append(tipoParam).append(" ").append(nombreParam);
                tiposParametros.add(tipoParam);
            }
        }

        String parametrosFinal = parametrosStr.length() == 0 ? "N/A" : parametrosStr.toString();

        // ERROR: Verificar si la función ya está declarada
        if (!tablaSimbolos.agregarSimbolo(nombreFuncion, tipoRetorno, "Función", linea, columna, "global",
                parametrosFinal)) {
            erroresSemanticos.add("Error: Función '" + nombreFuncion + "' ya declarada (línea " + linea + ")");
            return null;
        }

        tablaSimbolos.agregarInfoFuncion(nombreFuncion, tipoRetorno, tiposParametros);
        tablaSimbolos.entrarAmbitoFuncion(nombreFuncion);

        // Estado anterior para funciones anidadas
        String tipoRetornoAnterior = tipoRetornoFuncionActual;
        boolean funcionTieneReturnAnterior = funcionTieneReturn;
        boolean despuesDeReturnAnterior = despuesDeReturn;

        tipoRetornoFuncionActual = tipoRetorno;
        funcionTieneReturn = false;
        despuesDeReturn = false;

        // CORREGIR: Crear sets locales para la función
        Set<String> variablesDeclaradasLocales = new HashSet<>();
        Set<String> variablesUsadasLocales = new HashSet<>();
        Set<String> variablesInicializadasLocales = new HashSet<>();
        Map<String, Integer> lineasDeclaracionLocales = new HashMap<>();

        // Agregar TODOS los parámetros al scope local de la función (incluso
        // duplicados)
        if (ctx.parametros() != null) {
            for (MiLenguajeParser.ParametroContext param : ctx.parametros().parametro()) {
                String nombreParam = param.ID().getText();
                String tipoParam = param.tipo().getText();
                int lineaParam = param.ID().getSymbol().getLine();
                int columnaParam = param.ID().getSymbol().getCharPositionInLine();

                // SIEMPRE declarar el parámetro en la tabla de símbolos para que se pueda usar
                tablaSimbolos.declararParametro(nombreParam, tipoParam, lineaParam, columnaParam);

                // Agregar a las estructuras de seguimiento locales
                variablesDeclaradasLocales.add(nombreParam);
                variablesInicializadasLocales.add(nombreParam); // Los parámetros están inicializados
                lineasDeclaracionLocales.put(nombreParam, lineaParam);
            }
        }

        // Guardar estado global y usar el local
        Set<String> variablesDeclaradasAnterior = new HashSet<>(variablesDeclaradas);
        Set<String> variablesUsadasAnterior = new HashSet<>(variablesUsadas);
        Set<String> variablesInicializadasAnterior = new HashSet<>(variablesInicializadas);
        Map<String, Integer> lineasDeclaracionAnterior = new HashMap<>(lineasDeclaracion);

        // Usar las variables locales de la función
        variablesDeclaradas.clear();
        variablesDeclaradas.addAll(variablesDeclaradasLocales);
        variablesUsadas.clear();
        variablesUsadas.addAll(variablesUsadasLocales);
        variablesInicializadas.clear();
        variablesInicializadas.addAll(variablesInicializadasLocales);
        lineasDeclaracion.clear();
        lineasDeclaracion.putAll(lineasDeclaracionLocales);

        // Analizar el bloque de la función
        visit(ctx.bloque());

        // ERROR: Verificar que funciones no-void tengan return
        if (!tipoRetorno.equals("void") && !funcionTieneReturn) {
            erroresSemanticos.add("Error: Función '" + nombreFuncion + "' de tipo '" + tipoRetorno +
                    "' debe retornar un valor (línea " + linea + ")");
        }

        // WARNING: Verificar parámetros no usados - usar el conjunto original de
        // nombres únicos
        Set<String> parametrosUnicos = new HashSet<>();
        if (ctx.parametros() != null) {
            for (MiLenguajeParser.ParametroContext param : ctx.parametros().parametro()) {
                parametrosUnicos.add(param.ID().getText());
            }
        }

        for (String param : parametrosUnicos) {
            if (!variablesUsadas.contains(param)) {
                Integer lineaParam = lineasDeclaracion.get(param);
                if (lineaParam != null) {
                    agregarWarning("Warning: Parámetro '" + param + "' no usado en función '" +
                            nombreFuncion + "' (línea " + lineaParam + ")", lineaParam);
                }
            }
        }

        // WARNING: Verificar variables locales no usadas
        for (String variable : variablesDeclaradas) {
            // Solo verificar variables que no son parámetros
            if (!parametrosUnicos.contains(variable) && !variablesUsadas.contains(variable)) {
                Integer lineaVar = lineasDeclaracion.get(variable);
                if (lineaVar != null) {
                    agregarWarning(
                            "Warning: Variable '" + variable + "' declarada pero nunca usada (línea " + lineaVar + ")",
                            lineaVar);
                }
            }
        }

        // Restaurar estado anterior
        tipoRetornoFuncionActual = tipoRetornoAnterior;
        funcionTieneReturn = funcionTieneReturnAnterior;
        despuesDeReturn = despuesDeReturnAnterior;

        // Restaurar variables al estado anterior (scope global)
        variablesDeclaradas.clear();
        variablesDeclaradas.addAll(variablesDeclaradasAnterior);
        variablesUsadas.clear();
        variablesUsadas.addAll(variablesUsadasAnterior);
        variablesInicializadas.clear();
        variablesInicializadas.addAll(variablesInicializadasAnterior);
        lineasDeclaracion.clear();
        lineasDeclaracion.putAll(lineasDeclaracionAnterior);

        tablaSimbolos.salirAmbitoFuncion();
        return null;
    }

    /**
     * Maneja la función main y la registra en la tabla de símbolos
     */
    @Override
    public Void visitFuncionMain(MiLenguajeParser.FuncionMainContext ctx) {
        String tipoRetorno = ctx.tipo().getText();
        int linea = ctx.start.getLine();

        // Registrar función main en tabla de símbolos
        if (!tablaSimbolos.agregarSimbolo("main", tipoRetorno, "Función", linea, 0, "global", "N/A")) {
            erroresSemanticos.add("Error: Función 'main' ya declarada (línea " + linea + ")");
        }

        // Estado anterior
        String tipoRetornoAnterior = tipoRetornoFuncionActual;
        boolean funcionTieneReturnAnterior = funcionTieneReturn;
        boolean despuesDeReturnAnterior = despuesDeReturn;

        tipoRetornoFuncionActual = tipoRetorno;
        funcionTieneReturn = false;
        despuesDeReturn = false;

        // Entrar al ámbito main
        tablaSimbolos.entrarAmbitoFuncion("main");

        // Procesar el cuerpo de main
        visit(ctx.bloque());

        // ERROR: Verificar que main tenga return si no es void
        if (!tipoRetorno.equals("void") && !funcionTieneReturn) {
            erroresSemanticos.add(
                    "Error: Función main de tipo '" + tipoRetorno + "' debe retornar un valor (línea " + linea + ")");
        }

        // Restaurar estado anterior
        tipoRetornoFuncionActual = tipoRetornoAnterior;
        funcionTieneReturn = funcionTieneReturnAnterior;
        despuesDeReturn = despuesDeReturnAnterior;

        // Salir del ámbito main
        tablaSimbolos.salirAmbitoFuncion();
        return null;
    }

    /**
     * Visita llamadas a funciones y verifica parámetros
     */
    @Override
    public Void visitLlamadaExpr(MiLenguajeParser.LlamadaExprContext ctx) {
        String nombreFuncion = ctx.llamada().ID().getText();

        // ERROR: Verificar que la función existe
        if (!tablaSimbolos.existeFuncion(nombreFuncion)) {
            erroresSemanticos.add("Error: Función '" + nombreFuncion + "' no declarada (línea "
                    + ctx.start.getLine() + ")");
            return visitChildren(ctx);
        }

        // Obtener información de la función
        TablaSimbolos.FuncionInfo infoFuncion = tablaSimbolos.obtenerInfoFuncion(nombreFuncion);

        // ERROR: Verificar número de parámetros
        int parametrosEsperados = infoFuncion.getNumeroParametros();
        int parametrosProvidos = 0;

        if (ctx.llamada().argumentos() != null) {
            parametrosProvidos = ctx.llamada().argumentos().expresion().size();
        }

        if (parametrosProvidos != parametrosEsperados) {
            erroresSemanticos.add("Error: Función '" + nombreFuncion + "' espera " + parametrosEsperados
                    + " parámetros pero se proporcionaron " + parametrosProvidos + " (línea " + ctx.start.getLine()
                    + ")");
            return visitChildren(ctx);
        }

        // ERROR/WARNING: Verificar tipos de parámetros
        if (ctx.llamada().argumentos() != null) {
            for (int i = 0; i < parametrosProvidos; i++) {
                String tipoEsperado = infoFuncion.getTipoParametro(i);
                String tipoProvisto = obtenerTipoExpresion(ctx.llamada().argumentos().expresion(i));

                if (tipoProvisto != null && !tipoProvisto.equals(tipoEsperado)) {
                    if (esConversionSegura(tipoProvisto, tipoEsperado)) {
                        agregarWarning("Warning: Conversión implícita de " + tipoProvisto + " a " + tipoEsperado +
                                " en parámetro " + (i + 1) + " de función '" + nombreFuncion +
                                "' (línea " + ctx.start.getLine() + ")", ctx.start.getLine());
                    } else {
                        erroresSemanticos.add("Error: Tipo incompatible en parámetro " + (i + 1)
                                + " de función '" + nombreFuncion + "'. Se esperaba " + tipoEsperado
                                + " pero se obtuvo " + tipoProvisto + " (línea " + ctx.start.getLine() + ")");
                    }
                }
            }
        }

        return visitChildren(ctx);
    }

    /**
     * Visita cualquier sentencia y detecta incrementos/decrementos
     */
    @Override
    public Void visitSentencia(MiLenguajeParser.SentenciaContext ctx) {
        // NUEVO: Detectar incrementos/decrementos en el texto de la sentencia
        String textoSentencia = ctx.getText();
        
        // Detectar patrones como: variable++, variable--, ++variable, --variable
        if (textoSentencia.matches(".*\\w+\\+\\+.*") || textoSentencia.matches(".*\\+\\+\\w+.*") ||
            textoSentencia.matches(".*\\w+--.*") || textoSentencia.matches(".*--\\w+.*")) {
            
            // Extraer nombre de variable del patrón
            String nombreVariable = extraerVariableDeIncremento(textoSentencia);
            if (nombreVariable != null && tablaSimbolos.existe(nombreVariable)) {
                variablesUsadas.add(nombreVariable);
                variablesInicializadas.add(nombreVariable);
            }
        }

        // WARNING: Verificar código inalcanzable
        if (despuesDeReturn) {
            agregarWarning("Warning: Código inalcanzable después de return (línea " + ctx.start.getLine() + ")",
                    ctx.start.getLine());
        }

        // Chequeo de return para errores de tipo
        if (ctx.RETURN() != null) {
            funcionTieneReturn = true;
            despuesDeReturn = true;

            if (tipoRetornoFuncionActual != null) {
                if (tipoRetornoFuncionActual.equals("void")) {
                    // ERROR: Función void retornando valor
                    if (ctx.expresion() != null) {
                        erroresSemanticos.add("Error: Función void no puede retornar un valor (línea "
                                + ctx.start.getLine() + ")");
                    }
                } else {
                    // ERROR: Función no-void sin valor de retorno
                    if (ctx.expresion() == null) {
                        erroresSemanticos.add("Error: Función de tipo '" + tipoRetornoFuncionActual
                                + "' debe retornar un valor (línea " + ctx.start.getLine() + ")");
                    } else {
                        // ERROR/WARNING: Verificar tipo del valor de retorno
                        String tipoRetorno = obtenerTipoExpresion(ctx.expresion());
                        if (tipoRetorno != null && !tipoRetorno.equals(tipoRetornoFuncionActual)) {
                            if (esConversionSegura(tipoRetorno, tipoRetornoFuncionActual)) {
                                agregarWarning("Warning: Conversión implícita en return de " + tipoRetorno +
                                        " a " + tipoRetornoFuncionActual + " (línea " + ctx.start.getLine() + ")",
                                        ctx.start.getLine());
                            } else {
                                erroresSemanticos.add("Error: Tipo de retorno incompatible. Se esperaba "
                                        + tipoRetornoFuncionActual + " pero se obtuvo " + tipoRetorno
                                        + " (línea " + ctx.start.getLine() + ")");
                            }
                        }
                    }
                }
            }
        }

        // CAMBIO: Llamar directamente al método específico
        if (ctx.expresion() != null) {
            verificarOperadoresEnCondicion(ctx.expresion());
        }

        return visitChildren(ctx);
    }

    /**
     * Extrae el nombre de variable de expresiones de incremento/decremento
     */
    private String extraerVariableDeIncremento(String texto) {
        // Buscar patrones variable++ o variable--
        if (texto.matches(".*\\w+\\+\\+.*")) {
            String[] parts = texto.split("\\+\\+");
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }
        
        if (texto.matches(".*\\w+--.*")) {
            String[] parts = texto.split("--");
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }
        
        // Buscar patrones ++variable o --variable
        if (texto.matches(".*\\+\\+\\w+.*")) {
            String result = texto.replaceAll(".*\\+\\+(\\w+).*", "$1");
            return result;
        }
        
        if (texto.matches(".*--\\w+.*")) {
            String result = texto.replaceAll(".*--(\\w+).*", "$1");
            return result;
        }
        
        return null;
    }

    /**
     * Determina si una conversión de tipos es segura (sin pérdida de datos)
     */
    private boolean esConversionSegura(String tipoOrigen, String tipoDestino) {
        // int -> double es seguro
        if (tipoOrigen.equals("int") && tipoDestino.equals("double")) {
            return true;
        }
        // char -> int es seguro
        if (tipoOrigen.equals("char") && tipoDestino.equals("int")) {
            return true;
        }
        // char -> double es seguro
        if (tipoOrigen.equals("char") && tipoDestino.equals("double")) {
            return true;
        }
        return false;
    }

    /**
     * Obtiene el tipo de una expresión analizando su contexto
     */
    private String obtenerTipoExpresion(MiLenguajeParser.ExpresionContext ctx) {
        if (ctx instanceof MiLenguajeParser.NumExprContext) {
            String texto = ctx.getText();
            if (texto.contains("."))
                return "double";
            else
                return "int";
        }
        if (ctx instanceof MiLenguajeParser.CharExprContext) {
            return "char";
        }
        // NUEVO: Verificar si es un literal char (entre comillas simples)
        if (ctx.getText().startsWith("'") && ctx.getText().endsWith("'")) {
            return "char";
        }
        if (ctx instanceof MiLenguajeParser.IdExprContext) {
            String nombre = ((MiLenguajeParser.IdExprContext) ctx).ID().getText();
            return tablaSimbolos.obtenerTipo(nombre);
        }
        // Para llamadas a funciones
        if (ctx instanceof MiLenguajeParser.LlamadaExprContext) {
            String nombreFuncion = ((MiLenguajeParser.LlamadaExprContext) ctx).llamada().ID().getText();
            if (tablaSimbolos.existeFuncion(nombreFuncion)) {
                TablaSimbolos.FuncionInfo info = tablaSimbolos.obtenerInfoFuncion(nombreFuncion);
                return info.tipoRetorno;
            }
        }
        // Para operaciones aritméticas
        if (ctx instanceof MiLenguajeParser.MulDivExprContext) {
            MiLenguajeParser.MulDivExprContext mulDivCtx = (MiLenguajeParser.MulDivExprContext) ctx;
            return obtenerTipoExpresion(mulDivCtx.expresion(0));
        }
        if (ctx instanceof MiLenguajeParser.MasMenosExprContext) {
            MiLenguajeParser.MasMenosExprContext masMenosCtx = (MiLenguajeParser.MasMenosExprContext) ctx;
            return obtenerTipoExpresion(masMenosCtx.expresion(0));
        }
        return null;
    }

    /**
     * Maneja scopes de bloques creando nuevos contextos para variables locales
     */
    @Override
    public Void visitBloque(MiLenguajeParser.BloqueContext ctx) {
        tablaSimbolos.entrarScope();
        boolean anteriorDespuesDeReturn = despuesDeReturn;
        despuesDeReturn = false;

        super.visitChildren(ctx);

        despuesDeReturn = anteriorDespuesDeReturn;
        tablaSimbolos.salirScope();
        return null;
    }

    /**
     * Registra parámetros de función en la tabla de símbolos del scope de la
     * función
     */
    @Override
    public Void visitParametro(MiLenguajeParser.ParametroContext ctx) {
        String tipo = ctx.tipo().getText();
        String nombre = ctx.ID().getText();

        variablesDeclaradas.add(nombre);
        variablesInicializadas.add(nombre); // Los parámetros están inicializados por defecto
        lineasDeclaracion.put(nombre, ctx.start.getLine());

        tablaSimbolos.declararParametro(nombre, tipo, ctx.start.getLine(), ctx.start.getCharPositionInLine());

        return null;
    }

    /**
     * Agrega este método para obtener la tabla de símbolos
     */
    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    @Override
    public Void visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
        return super.visitChildren(node);
    }

    /**
     * Maneja scopes de bucles for con variables locales
     */
    @Override
    public Void visitForStmt(MiLenguajeParser.ForStmtContext ctx) {
        tablaSimbolos.entrarScope();

        // Procesar la inicialización del for
        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        // Verificar la condición del for si existe
        if (ctx.expresion() != null) {
            verificarOperadoresEnCondicion(ctx.expresion());
            visit(ctx.expresion());
        }

        // Procesar el cuerpo del for
        visit(ctx.sentencia());

        tablaSimbolos.salirScope();
        return null;
    }

    /**
     * Procesa inicializaciones en bucles for, manejando tanto declaraciones como
     * asignaciones
     */
    @Override
    public Void visitForInit(MiLenguajeParser.ForInitContext ctx) {
        if (ctx.tipo() != null && ctx.listaDeclaracion() != null) {
            // Es una declaración en el for (como: int i = 0)
            String tipo = ctx.tipo().getText();
            MiLenguajeParser.ListaDeclaracionContext lista = ctx.listaDeclaracion();

            for (MiLenguajeParser.DeclaradorContext declaradorCtx : lista.declarador()) {
                String nombre = declaradorCtx.ID().getText();
                int linea = declaradorCtx.ID().getSymbol().getLine();
                int columna = declaradorCtx.ID().getSymbol().getCharPositionInLine();

                variablesDeclaradas.add(nombre);
                lineasDeclaracion.put(nombre, linea);

                if (!tablaSimbolos.declarar(nombre, tipo, linea, columna)) {
                    erroresSemanticos
                            .add("Error: Variable '" + nombre + "' ya declarada en este ámbito (línea " + linea + ")");
                }

                if (declaradorCtx.expresion() != null) {
                    visit(declaradorCtx.expresion());
                    variablesInicializadas.add(nombre);
                }
            }
        } else if (ctx.asignacion() != null) {
            // Es una asignación en el for (como: i = 0)
            visit(ctx.asignacion());
        }
        return null;
    }

    /**
     * Visita sentencias IF y verifica posibles errores de operadores
     */
    @Override
    public Void visitIfStmt(MiLenguajeParser.IfStmtContext ctx) {
        // Verificar la condición del if
        if (ctx.expresion() != null) {
            // Verificar errores comunes de operadores
            verificarOperadoresEnCondicion(ctx.expresion());

            // Procesar la expresión normalmente
            visit(ctx.expresion());
        }

        // Procesar el resto de la sentencia if
        if (ctx.sentencia() != null) {
            for (MiLenguajeParser.SentenciaContext sentencia : ctx.sentencia()) {
                visit(sentencia);
            }
        }

        return null;
    }

    /**
     * Verifica errores comunes de operadores en condiciones (IF, WHILE, FOR)
     */
    private void verificarOperadoresEnCondicion(MiLenguajeParser.ExpresionContext ctx) {
        String textoExpresion = ctx.getText();
        int linea = ctx.start.getLine();

        // 1. ASIGNACIÓN EN LUGAR DE COMPARACIÓN: = en lugar de ==
        if (textoExpresion.matches(".*\\w+\\s*=\\s*\\w+.*") ||
                textoExpresion.matches(".*\\w+\\s*=\\s*\\d+.*")) {

            // Verificar que no sea ==, !=, <=, >=, += etc.
            if (!textoExpresion.contains("==") && !textoExpresion.contains("!=")
                    && !textoExpresion.contains("<=") && !textoExpresion.contains(">=")
                    && !textoExpresion.contains("+=") && !textoExpresion.contains("-=")
                    && !textoExpresion.contains("*=") && !textoExpresion.contains("/=")) {

                agregarWarning("Warning: Posible asignación en lugar de comparación (línea " + linea +
                        ").", linea);
            }
        }

        // 2. NEGACIÓN INCOMPLETA: ! solo en lugar de !=
        if (textoExpresion.matches(".*!\\s*\\w+.*") && !textoExpresion.contains("!=")) {
            // Verificar que no sea una negación lógica válida como !variable
            if (textoExpresion.matches(".*!\\s*\\w+\\s*[=<>].*")) {
                agregarWarning("Warning: Posible uso de '!' en lugar de '!=' (línea " + linea +
                        ").", linea);
            }
        }

        // 3. OPERADORES ARITMÉTICOS EN CONTEXTO LÓGICO
        verificarOperadoresAritmeticosEnLogico(textoExpresion, linea);

        // 4. COMPARACIONES MAL FORMADAS
        verificarComparacionesMalFormadas(textoExpresion, linea);

        // 5. OPERADORES DE BITS EN LUGAR DE LÓGICOS
        verificarOperadoresBitsEnLogico(textoExpresion, linea);
    }

    /**
     * Verifica operadores aritméticos usados incorrectamente en contexto lógico
     */
    private void verificarOperadoresAritmeticosEnLogico(String texto, int linea) {
        // Detectar + o - entre variables/números donde debería haber && o ||
        if (texto.matches(".*\\w+\\s*\\+\\s*\\w+.*") && !texto.contains("return")) {
            // Solo advertir si parece ser una condición
            if (texto.matches(".*\\w+\\s*\\+\\s*\\w+\\s*[<>=].*") ||
                    texto.matches(".*[<>=].*\\w+\\s*\\+\\s*\\w+.*")) {
                agregarWarning("Warning: Operador '+' en condición lógica. (línea " + linea + ")",
                        linea);
            }
        }

        if (texto.matches(".*\\w+\\s*-\\s*\\w+.*") && !texto.contains("return")) {
            if (texto.matches(".*\\w+\\s*-\\s*\\w+\\s*[<>=].*") ||
                    texto.matches(".*[<>=].*\\w+\\s*-\\s*\\w+.*")) {
                agregarWarning("Warning: Operador '-' en condición lógica. (línea "
                        + linea + ")", linea);
            }
        }

        // Detectar * usado donde debería ser &&
        if (texto.matches(".*\\w+\\s*\\*\\s*\\w+.*") && !texto.contains("return")) {
            if (texto.matches(".*\\w+\\s*\\*\\s*\\w+\\s*[<>=].*") ||
                    texto.matches(".*[<>=].*\\w+\\s*\\*\\s*\\w+.*")) {
                agregarWarning("Warning: Operador '*' en condición lógica. (línea " + linea + ")",
                        linea);
            }
        }
    }

    /**
     * Verifica comparaciones mal formadas
     */
    private void verificarComparacionesMalFormadas(String texto, int linea) {
        // Detectar => en lugar de >=
        if (texto.contains("=>")) {
            agregarWarning("Warning: Operador '=>' no válido. (línea " + linea + ")", linea);
        }

        // Detectar =< en lugar de <=
        if (texto.contains("=<")) {
            agregarWarning("Warning: Operador '=<' no válido. (línea " + linea + ")", linea);
        }

        // Detectar === (JavaScript) en lugar de ==
        if (texto.contains("===")) {
            agregarWarning("Warning: Operador '===' no válido en C++. (línea " + linea + ")", linea);
        }

        // Detectar !== (JavaScript) en lugar de !=
        if (texto.contains("!==")) {
            agregarWarning("Warning: Operador '!==' no válido en C++. (línea " + linea + ")", linea);
        }
    }

    /**
     * Verifica operadores de bits usados incorrectamente en lugar de lógicos
     */
    private void verificarOperadoresBitsEnLogico(String texto, int linea) {
        // Detectar & en lugar de &&
        if (texto.matches(".*\\w+\\s*&\\s*\\w+.*") && !texto.contains("&&")) {
            agregarWarning("Warning: Operador '&' (bitwise). (línea " + linea + ")", linea);
        }

        // Detectar | en lugar de ||
        if (texto.matches(".*\\w+\\s*\\|\\s*\\w+.*") && !texto.contains("||")) {
            agregarWarning("Warning: Operador '|' (bitwise). (línea " + linea + ")", linea);
        }
    }

    /**
     * Aplica verificaciones también a bucles WHILE
     */
    @Override
    public Void visitWhileStmt(MiLenguajeParser.WhileStmtContext ctx) {
        // Verificar la condición del while
        if (ctx.expresion() != null) {
            verificarOperadoresEnCondicion(ctx.expresion());
            visit(ctx.expresion());
        }

        // Procesar el cuerpo del while
        if (ctx.sentencia() != null) {
            visit(ctx.sentencia());
        }

        return null;
    }
}
