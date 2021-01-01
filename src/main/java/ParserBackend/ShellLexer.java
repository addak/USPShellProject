// Generated from /home/abhi/JavaProjects/USPShellParser/src/Shell.g4 by ANTLR 4.9
package ParserBackend;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ShellLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SPACE=1, PARAMS=2, ARGUMENTS=3, STRING=4, WORD=5;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"SPACE", "PARAMS", "ARGUMENTS", "STRING", "WORD"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SPACE", "PARAMS", "ARGUMENTS", "STRING", "WORD"
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


	public ShellLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Shell.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\7\61\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\6\2\17\n\2\r\2\16\2\20\3\2\3\2\3"+
		"\3\3\3\6\3\27\n\3\r\3\16\3\30\3\4\3\4\7\4\35\n\4\f\4\16\4 \13\4\3\5\3"+
		"\5\3\5\3\5\7\5&\n\5\f\5\16\5)\13\5\3\5\3\5\3\6\6\6.\n\6\r\6\16\6/\2\2"+
		"\7\3\3\5\4\7\5\t\6\13\7\3\2\6\4\2\13\13\"\"\3\2c|\4\2$$^^\7\2\13\f\17"+
		"\17\"\"$$,,\2\67\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\3\16\3\2\2\2\5\24\3\2\2\2\7\36\3\2\2\2\t!\3\2\2\2\13-\3\2\2\2"+
		"\r\17\t\2\2\2\16\r\3\2\2\2\17\20\3\2\2\2\20\16\3\2\2\2\20\21\3\2\2\2\21"+
		"\22\3\2\2\2\22\23\b\2\2\2\23\4\3\2\2\2\24\26\7/\2\2\25\27\t\3\2\2\26\25"+
		"\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31\6\3\2\2\2\32\35"+
		"\5\t\5\2\33\35\5\13\6\2\34\32\3\2\2\2\34\33\3\2\2\2\35 \3\2\2\2\36\34"+
		"\3\2\2\2\36\37\3\2\2\2\37\b\3\2\2\2 \36\3\2\2\2!\'\7$\2\2\"&\n\4\2\2#"+
		"$\7^\2\2$&\t\4\2\2%\"\3\2\2\2%#\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2"+
		"\2(*\3\2\2\2)\'\3\2\2\2*+\7$\2\2+\n\3\2\2\2,.\n\5\2\2-,\3\2\2\2./\3\2"+
		"\2\2/-\3\2\2\2/\60\3\2\2\2\60\f\3\2\2\2\n\2\20\30\34\36%\'/\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}