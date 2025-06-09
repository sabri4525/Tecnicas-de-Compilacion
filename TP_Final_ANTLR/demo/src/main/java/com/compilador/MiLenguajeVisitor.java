// Generated from src/main/antlr4/com/compilador/MiLenguaje.g4 by ANTLR 4.13.2
package com.compilador;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MiLenguajeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MiLenguajeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(MiLenguajeParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#inicio}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio(MiLenguajeParser.InicioContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentenciaInicial}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaInicial(MiLenguajeParser.SentenciaInicialContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#listaDeclaracion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaDeclaracion(MiLenguajeParser.ListaDeclaracionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#declarador}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarador(MiLenguajeParser.DeclaradorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncion(MiLenguajeParser.FuncionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#parametros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametros(MiLenguajeParser.ParametrosContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#parametro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametro(MiLenguajeParser.ParametroContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#bloque}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloque(MiLenguajeParser.BloqueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentencia(MiLenguajeParser.SentenciaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(MiLenguajeParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(MiLenguajeParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(MiLenguajeParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(MiLenguajeParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(MiLenguajeParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#llamada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlamada(MiLenguajeParser.LlamadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#argumentos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentos(MiLenguajeParser.ArgumentosContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDivExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivExpr(MiLenguajeParser.MulDivExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpr(MiLenguajeParser.IdExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Relacional}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelacional(MiLenguajeParser.RelacionalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumExpr(MiLenguajeParser.NumExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentesis(MiLenguajeParser.ParentesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IgualExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIgualExpr(MiLenguajeParser.IgualExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MasMenosExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasMenosExpr(MiLenguajeParser.MasMenosExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharExpr(MiLenguajeParser.CharExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LlamadaExpr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlamadaExpr(MiLenguajeParser.LlamadaExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Logica}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogica(MiLenguajeParser.LogicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(MiLenguajeParser.TipoContext ctx);
}