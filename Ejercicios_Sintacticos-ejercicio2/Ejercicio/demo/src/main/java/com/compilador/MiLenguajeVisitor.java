// Generated from com\compilador\MiLenguaje.g4 by ANTLR 4.9.3
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
	 * Visit a parse tree produced by the {@code asignacion}
	 * labeled alternative in {@link MiLenguajeParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(MiLenguajeParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sentenciaIf}
	 * labeled alternative in {@link MiLenguajeParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sentenciaVacia}
	 * labeled alternative in {@link MiLenguajeParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaVacia(MiLenguajeParser.SentenciaVaciaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suma}
	 * labeled alternative in {@link MiLenguajeParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuma(MiLenguajeParser.SumaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code termino}
	 * labeled alternative in {@link MiLenguajeParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermino(MiLenguajeParser.TerminoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identificador}
	 * labeled alternative in {@link MiLenguajeParser#t}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentificador(MiLenguajeParser.IdentificadorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numero}
	 * labeled alternative in {@link MiLenguajeParser#t}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumero(MiLenguajeParser.NumeroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link MiLenguajeParser#t}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(MiLenguajeParser.ParensContext ctx);
}