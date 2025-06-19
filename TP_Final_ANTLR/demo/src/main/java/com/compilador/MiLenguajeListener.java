// Generated from src/main/antlr4/com/compilador/MiLenguaje.g4 by ANTLR 4.13.2
package com.compilador;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiLenguajeParser}.
 */
public interface MiLenguajeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(MiLenguajeParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(MiLenguajeParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#sentenciaInicial}.
	 * @param ctx the parse tree
	 */
	void enterSentenciaInicial(MiLenguajeParser.SentenciaInicialContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#sentenciaInicial}.
	 * @param ctx the parse tree
	 */
	void exitSentenciaInicial(MiLenguajeParser.SentenciaInicialContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#funcionMain}.
	 * @param ctx the parse tree
	 */
	void enterFuncionMain(MiLenguajeParser.FuncionMainContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#funcionMain}.
	 * @param ctx the parse tree
	 */
	void exitFuncionMain(MiLenguajeParser.FuncionMainContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#funcion}.
	 * @param ctx the parse tree
	 */
	void enterFuncion(MiLenguajeParser.FuncionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#funcion}.
	 * @param ctx the parse tree
	 */
	void exitFuncion(MiLenguajeParser.FuncionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#parametros}.
	 * @param ctx the parse tree
	 */
	void enterParametros(MiLenguajeParser.ParametrosContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#parametros}.
	 * @param ctx the parse tree
	 */
	void exitParametros(MiLenguajeParser.ParametrosContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#parametro}.
	 * @param ctx the parse tree
	 */
	void enterParametro(MiLenguajeParser.ParametroContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#parametro}.
	 * @param ctx the parse tree
	 */
	void exitParametro(MiLenguajeParser.ParametroContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#bloque}.
	 * @param ctx the parse tree
	 */
	void enterBloque(MiLenguajeParser.BloqueContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#bloque}.
	 * @param ctx the parse tree
	 */
	void exitBloque(MiLenguajeParser.BloqueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 */
	void enterSentencia(MiLenguajeParser.SentenciaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 */
	void exitSentencia(MiLenguajeParser.SentenciaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracion(MiLenguajeParser.DeclaracionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracion(MiLenguajeParser.DeclaracionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#listaDeclaracion}.
	 * @param ctx the parse tree
	 */
	void enterListaDeclaracion(MiLenguajeParser.ListaDeclaracionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#listaDeclaracion}.
	 * @param ctx the parse tree
	 */
	void exitListaDeclaracion(MiLenguajeParser.ListaDeclaracionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#declarador}.
	 * @param ctx the parse tree
	 */
	void enterDeclarador(MiLenguajeParser.DeclaradorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#declarador}.
	 * @param ctx the parse tree
	 */
	void exitDeclarador(MiLenguajeParser.DeclaradorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void enterAsignacion(MiLenguajeParser.AsignacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void exitAsignacion(MiLenguajeParser.AsignacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(MiLenguajeParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(MiLenguajeParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(MiLenguajeParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(MiLenguajeParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(MiLenguajeParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(MiLenguajeParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(MiLenguajeParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(MiLenguajeParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#llamada}.
	 * @param ctx the parse tree
	 */
	void enterLlamada(MiLenguajeParser.LlamadaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#llamada}.
	 * @param ctx the parse tree
	 */
	void exitLlamada(MiLenguajeParser.LlamadaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#argumentos}.
	 * @param ctx the parse tree
	 */
	void enterArgumentos(MiLenguajeParser.ArgumentosContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#argumentos}.
	 * @param ctx the parse tree
	 */
	void exitArgumentos(MiLenguajeParser.ArgumentosContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDivExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterMulDivExpr(MiLenguajeParser.MulDivExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDivExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitMulDivExpr(MiLenguajeParser.MulDivExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterIdExpr(MiLenguajeParser.IdExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitIdExpr(MiLenguajeParser.IdExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Relacional}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterRelacional(MiLenguajeParser.RelacionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Relacional}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitRelacional(MiLenguajeParser.RelacionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterNumExpr(MiLenguajeParser.NumExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitNumExpr(MiLenguajeParser.NumExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterParentesis(MiLenguajeParser.ParentesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitParentesis(MiLenguajeParser.ParentesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IgualExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterIgualExpr(MiLenguajeParser.IgualExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IgualExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitIgualExpr(MiLenguajeParser.IgualExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MasMenosExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterMasMenosExpr(MiLenguajeParser.MasMenosExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MasMenosExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitMasMenosExpr(MiLenguajeParser.MasMenosExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterCharExpr(MiLenguajeParser.CharExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitCharExpr(MiLenguajeParser.CharExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LlamadaExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterLlamadaExpr(MiLenguajeParser.LlamadaExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LlamadaExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitLlamadaExpr(MiLenguajeParser.LlamadaExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Logica}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterLogica(MiLenguajeParser.LogicaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Logica}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitLogica(MiLenguajeParser.LogicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiLenguajeParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(MiLenguajeParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(MiLenguajeParser.TipoContext ctx);
}