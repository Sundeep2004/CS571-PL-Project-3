public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }
    private Automaton makeSingleCharAutomaton(char c) {
        Automaton a = new AutomatonImpl();
        a.addState(0, true, false);
        a.addState(1, false, true);
        a.addTransition(0, c, 1);
        return a;
    }

    @Override
    protected void init_lexer() {
    
        // NUM: [0-9]*\.[0-9]+
        Automaton num = new AutomatonImpl();
        int state = 0;
        num.addState(state, true, false);        // 0
        for (int i = 1; i <= 10; i++) {
            num.addState(i, false, false);       // 1-10
        }
        num.addState(11, false, true);           // accepting state

        // loop for digits [0-9]* → state 0 to 0
        for (char d = '0'; d <= '9'; d++) {
            num.addTransition(0, d, 0);
        }

        // dot
        num.addTransition(0, '.', 1);

        // [0-9]+ after dot
        for (char d = '0'; d <= '9'; d++) {
            num.addTransition(1, d, 2);  // at least one digit
            num.addTransition(2, d, 2);  // loop
        }

        num.addState(2, false, true); // Accepting after valid float

        // Add automata
        lex = new LexerImpl();

        lex.add_automaton(TokenType.NUM, num);
        lex.add_automaton(TokenType.PLUS, makeSingleCharAutomaton('+'));
        lex.add_automaton(TokenType.MINUS, makeSingleCharAutomaton('-'));
        lex.add_automaton(TokenType.TIMES, makeSingleCharAutomaton('*'));
        lex.add_automaton(TokenType.DIV, makeSingleCharAutomaton('/'));
        lex.add_automaton(TokenType.LPAREN, makeSingleCharAutomaton('('));
        lex.add_automaton(TokenType.RPAREN, makeSingleCharAutomaton(')'));

        // WHITE_SPACE: (' '|\n|\r|\t)*
        Automaton ws = new AutomatonImpl();
        ws.addState(0, true, true); // start is accepting
        ws.addTransition(0, ' ', 0);
        ws.addTransition(0, '\n', 0);
        ws.addTransition(0, '\r', 0);
        ws.addTransition(0, '\t', 0);
        lex.add_automaton(TokenType.WHITE_SPACE, ws);
    }
}