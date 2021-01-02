// Generated from /home/abhi/JavaProjects/USPShellProject/src/main/java/Shell.g4 by ANTLR 4.9
package ParserBackend;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ShellLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SPACE=1, PARAMS=2, VALUES=3, ARGUMENTS=4, STRING=5, WORD=6;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"SPACE", "PARAMS", "VALUES", "ARGUMENTS", "STRING", "WORD"
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
			null, "SPACE", "PARAMS", "VALUES", "ARGUMENTS", "STRING", "WORD"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\b@\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\6\2\21\n\2\r\2\16\2\22\3\2"+
		"\3\2\3\3\3\3\3\3\3\3\3\3\6\3\34\n\3\r\3\16\3\35\5\3 \n\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\5\4(\n\4\3\5\3\5\7\5,\n\5\f\5\16\5/\13\5\3\6\3\6\3\6\3\6\7"+
		"\6\65\n\6\f\6\16\68\13\6\3\6\3\6\3\7\6\7=\n\7\r\7\16\7>\2\2\b\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\3\2\b\4\2\13\13\"\"\4\2--//\4\2ttyz\3\2c|\4\2$$^^\7"+
		"\2\13\f\17\17\"\"$$,,\2I\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\3\20\3\2\2\2\5\37\3\2\2\2\7\'\3\2\2\2\t-\3"+
		"\2\2\2\13\60\3\2\2\2\r<\3\2\2\2\17\21\t\2\2\2\20\17\3\2\2\2\21\22\3\2"+
		"\2\2\22\20\3\2\2\2\22\23\3\2\2\2\23\24\3\2\2\2\24\25\b\2\2\2\25\4\3\2"+
		"\2\2\26\27\t\3\2\2\27\30\t\4\2\2\30 \5\7\4\2\31\33\7/\2\2\32\34\t\5\2"+
		"\2\33\32\3\2\2\2\34\35\3\2\2\2\35\33\3\2\2\2\35\36\3\2\2\2\36 \3\2\2\2"+
		"\37\26\3\2\2\2\37\31\3\2\2\2 \6\3\2\2\2!\"\t\4\2\2\"(\5\7\4\2#$\t\3\2"+
		"\2$%\t\4\2\2%(\5\7\4\2&(\3\2\2\2\'!\3\2\2\2\'#\3\2\2\2\'&\3\2\2\2(\b\3"+
		"\2\2\2),\5\13\6\2*,\5\r\7\2+)\3\2\2\2+*\3\2\2\2,/\3\2\2\2-+\3\2\2\2-."+
		"\3\2\2\2.\n\3\2\2\2/-\3\2\2\2\60\66\7$\2\2\61\65\n\6\2\2\62\63\7^\2\2"+
		"\63\65\t\6\2\2\64\61\3\2\2\2\64\62\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66"+
		"\67\3\2\2\2\679\3\2\2\28\66\3\2\2\29:\7$\2\2:\f\3\2\2\2;=\n\7\2\2<;\3"+
		"\2\2\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?\16\3\2\2\2\f\2\22\35\37\'+-\64\66"+
		">\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}