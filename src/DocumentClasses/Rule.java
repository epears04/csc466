package DocumentClasses;

public class Rule {

    private ItemSet left, right;

    public Rule(ItemSet left, ItemSet right) {
        this.left = left;
        this.right = right;
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
