// Generated from MiLenguaje.g4 by ANTLR 4.13.2
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
	 * Enter a parse tree produced by {@link MiLenguajeParser#token}.
	 * @param ctx the parse tree
	 */
	void enterToken(MiLenguajeParser.TokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiLenguajeParser#token}.
	 * @param ctx the parse tree
	 */
	void exitToken(MiLenguajeParser.TokenContext ctx);
}