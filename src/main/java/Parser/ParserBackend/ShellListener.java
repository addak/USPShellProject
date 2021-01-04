package Parser.ParserBackend;// Generated from /home/addak/Java/Java Projects/USPShellProject/src/main/java/Shell.g4 by ANTLR 4.9
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ShellParser}.
 */
public interface ShellListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ShellParser#input}.
	 * @param ctx the parse tree
	 */
	void enterInput(ShellParser.InputContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShellParser#input}.
	 * @param ctx the parse tree
	 */
	void exitInput(ShellParser.InputContext ctx);
}