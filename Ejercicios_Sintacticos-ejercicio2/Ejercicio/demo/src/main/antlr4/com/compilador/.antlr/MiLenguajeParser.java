// Generated from c:/Users/Sabri/Downloads/Ejercicios_Sintacticos-ejercicio2/Ejercicio/demo/src/main/antlr4/com/compilador/MiLenguaje.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MiLenguajeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PA=1, PC=2, PYC=3, IGUAL=4, SUM=5, IF=6, ID=7, NUM=8, WS=9;
	public static final int
		RULE_programa = 0, RULE_s = 1, RULE_e = 2, RULE_t = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "s", "e", "t"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "';'", "'='", "'+'", "'if'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PA", "PC", "PYC", "IGUAL", "SUM", "IF", "ID", "NUM", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MiLenguaje.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MiLenguajeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramaContext extends ParserRuleContext {
		public SContext s() {
			return getRuleContext(SContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MiLenguajeParser.EOF, 0); }
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			s();
			setState(9);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SContext extends ParserRuleContext {
		public SContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_s; }
	 
		public SContext() { }
		public void copyFrom(SContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AsignacionContext extends SContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode IGUAL() { return getToken(MiLenguajeParser.IGUAL, 0); }
		public EContext e() {
			return getRuleContext(EContext.class,0);
		}
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public AsignacionContext(SContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SentenciaIfContext extends SContext {
		public TerminalNode IF() { return getToken(MiLenguajeParser.IF, 0); }
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public EContext e() {
			return getRuleContext(EContext.class,0);
		}
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public SContext s() {
			return getRuleContext(SContext.class,0);
		}
		public SentenciaIfContext(SContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SentenciaVaciaContext extends SContext {
		public SentenciaVaciaContext(SContext ctx) { copyFrom(ctx); }
	}

	public final SContext s() throws RecognitionException {
		SContext _localctx = new SContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_s);
		try {
			setState(23);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new AsignacionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(11);
				match(ID);
				setState(12);
				match(IGUAL);
				setState(13);
				e(0);
				setState(14);
				match(PYC);
				}
				break;
			case IF:
				_localctx = new SentenciaIfContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(16);
				match(IF);
				setState(17);
				match(PA);
				setState(18);
				e(0);
				setState(19);
				match(PC);
				setState(20);
				s();
				}
				break;
			case EOF:
				_localctx = new SentenciaVaciaContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EContext extends ParserRuleContext {
		public EContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_e; }
	 
		public EContext() { }
		public void copyFrom(EContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SumaContext extends EContext {
		public EContext e() {
			return getRuleContext(EContext.class,0);
		}
		public TerminalNode SUM() { return getToken(MiLenguajeParser.SUM, 0); }
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public SumaContext(EContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TerminoContext extends EContext {
		public TContext t() {
			return getRuleContext(TContext.class,0);
		}
		public TerminoContext(EContext ctx) { copyFrom(ctx); }
	}

	public final EContext e() throws RecognitionException {
		return e(0);
	}

	private EContext e(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		EContext _localctx = new EContext(_ctx, _parentState);
		EContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_e, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new TerminoContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(26);
			t();
			}
			_ctx.stop = _input.LT(-1);
			setState(33);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new SumaContext(new EContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_e);
					setState(28);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(29);
					match(SUM);
					setState(30);
					t();
					}
					} 
				}
				setState(35);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TContext extends ParserRuleContext {
		public TContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_t; }
	 
		public TContext() { }
		public void copyFrom(TContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParensContext extends TContext {
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public EContext e() {
			return getRuleContext(EContext.class,0);
		}
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public ParensContext(TContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumeroContext extends TContext {
		public TerminalNode NUM() { return getToken(MiLenguajeParser.NUM, 0); }
		public NumeroContext(TContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdentificadorContext extends TContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public IdentificadorContext(TContext ctx) { copyFrom(ctx); }
	}

	public final TContext t() throws RecognitionException {
		TContext _localctx = new TContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_t);
		try {
			setState(42);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new IdentificadorContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(36);
				match(ID);
				}
				break;
			case NUM:
				_localctx = new NumeroContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(37);
				match(NUM);
				}
				break;
			case PA:
				_localctx = new ParensContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(38);
				match(PA);
				setState(39);
				e(0);
				setState(40);
				match(PC);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return e_sempred((EContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean e_sempred(EContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\t-\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\u0018\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0005\u0002 \b\u0002\n\u0002\f\u0002#\t"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0003\u0003+\b\u0003\u0001\u0003\u0000\u0001\u0004\u0004\u0000"+
		"\u0002\u0004\u0006\u0000\u0000-\u0000\b\u0001\u0000\u0000\u0000\u0002"+
		"\u0017\u0001\u0000\u0000\u0000\u0004\u0019\u0001\u0000\u0000\u0000\u0006"+
		"*\u0001\u0000\u0000\u0000\b\t\u0003\u0002\u0001\u0000\t\n\u0005\u0000"+
		"\u0000\u0001\n\u0001\u0001\u0000\u0000\u0000\u000b\f\u0005\u0007\u0000"+
		"\u0000\f\r\u0005\u0004\u0000\u0000\r\u000e\u0003\u0004\u0002\u0000\u000e"+
		"\u000f\u0005\u0003\u0000\u0000\u000f\u0018\u0001\u0000\u0000\u0000\u0010"+
		"\u0011\u0005\u0006\u0000\u0000\u0011\u0012\u0005\u0001\u0000\u0000\u0012"+
		"\u0013\u0003\u0004\u0002\u0000\u0013\u0014\u0005\u0002\u0000\u0000\u0014"+
		"\u0015\u0003\u0002\u0001\u0000\u0015\u0018\u0001\u0000\u0000\u0000\u0016"+
		"\u0018\u0001\u0000\u0000\u0000\u0017\u000b\u0001\u0000\u0000\u0000\u0017"+
		"\u0010\u0001\u0000\u0000\u0000\u0017\u0016\u0001\u0000\u0000\u0000\u0018"+
		"\u0003\u0001\u0000\u0000\u0000\u0019\u001a\u0006\u0002\uffff\uffff\u0000"+
		"\u001a\u001b\u0003\u0006\u0003\u0000\u001b!\u0001\u0000\u0000\u0000\u001c"+
		"\u001d\n\u0002\u0000\u0000\u001d\u001e\u0005\u0005\u0000\u0000\u001e "+
		"\u0003\u0006\u0003\u0000\u001f\u001c\u0001\u0000\u0000\u0000 #\u0001\u0000"+
		"\u0000\u0000!\u001f\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000"+
		"\"\u0005\u0001\u0000\u0000\u0000#!\u0001\u0000\u0000\u0000$+\u0005\u0007"+
		"\u0000\u0000%+\u0005\b\u0000\u0000&\'\u0005\u0001\u0000\u0000\'(\u0003"+
		"\u0004\u0002\u0000()\u0005\u0002\u0000\u0000)+\u0001\u0000\u0000\u0000"+
		"*$\u0001\u0000\u0000\u0000*%\u0001\u0000\u0000\u0000*&\u0001\u0000\u0000"+
		"\u0000+\u0007\u0001\u0000\u0000\u0000\u0003\u0017!*";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}