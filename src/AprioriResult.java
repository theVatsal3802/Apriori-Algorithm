import java.util.List;
import java.util.Set;

class AprioriResult {
    List<Set<String>> largeItemSets;
    List<String> associationRules;

    public AprioriResult(List<Set<String>> largeItemSets, List<String> associationRules) {
        this.largeItemSets = largeItemSets;
        this.associationRules = associationRules;
    }

    public List<Set<String>> getLargeItemSets() {
        return largeItemSets;
    }

    public List<String> getAssociationRules() {
        return associationRules;
    }
}