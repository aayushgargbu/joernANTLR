package ast.statements;

import ast.walking.ASTNodeVisitor;

public class ThrowStatement extends JumpStatement {
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
