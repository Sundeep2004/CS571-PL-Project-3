public class ParserImpl extends Parser {

    @Override
    public Expr do_parse() throws Exception {
        Expr result = parseT();
        if (tokens != null) {
            throw new Exception("Unexpected token at end: " + tokens.elem.lexeme);
        }
        return result;
    }

    private Expr parseT() throws Exception {
        Expr left = parseF();

        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = parseAddOp();
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } else {
                return new MinusExpr(left, right);
            }
        } else {
            return left;
        }
    }

    private Expr parseF() throws Exception {
        Expr left = parseLit();

        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = parseMulOp();
            Expr right = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right);
            }
        } else {
            return left;
        }
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token num = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(num.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr inside = parseT();
            consume(TokenType.RPAREN);
            return inside;
        } else {
            throw new Exception("Expected literal or parenthesized expression");
        }
    }

    private Token parseAddOp() throws Exception {
        if (peek(TokenType.PLUS, 0)) {
            return consume(TokenType.PLUS);
        } else if (peek(TokenType.MINUS, 0)) {
            return consume(TokenType.MINUS);
        } else {
            throw new Exception("Expected '+' or '-'");
        }
    }

    private Token parseMulOp() throws Exception {
        if (peek(TokenType.TIMES, 0)) {
            return consume(TokenType.TIMES);
        } else if (peek(TokenType.DIV, 0)) {
            return consume(TokenType.DIV);
        } else {
            throw new Exception("Expected '*' or '/'");
        }
    }
}