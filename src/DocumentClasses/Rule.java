package DocumentClasses;

public class Rule {

    private ItemSet left, right, root;

    public Rule(ItemSet left, ItemSet right, ItemSet root) {
        this.left = left;
        this.right = right;
        this.root = root;
    }

    public ItemSet getLeft() {
        return left;
    }

    public ItemSet getRight() {
        return right;
    }

    public ItemSet getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return left.toString() + "->" + right.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return left.equals(rule.left) && right.equals(rule.right); //TODO: check
    }
}
