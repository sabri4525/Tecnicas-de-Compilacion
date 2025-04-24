// Generated from c:/Users/Sabri/Tecnicas-de-Compilacion/compilador-proyecto/src/main/antlr4/com/compilador/parser/MyGrammar.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MyGrammarParser}.
 */
public interface MyGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MyGrammarParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(MyGrammarParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyGrammarParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(MyGrammarParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyGrammarParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(MyGrammarParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyGrammarParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(MyGrammarParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(MyGrammarParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(MyGrammarParser.ExprContext ctx);
}