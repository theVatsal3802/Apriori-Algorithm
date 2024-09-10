import java.util.*;

class Apriori {
    private final List<Task> transactions;
    private final double minSupport;
    private final double minConfidence;
    private final int totalTransactions;

    public Apriori(List<Task> transactions, double minSupport, double minConfidence) {
        this.transactions = transactions;
        this.minSupport = minSupport / 100;
        this.minConfidence = minConfidence / 100;
        this.totalTransactions = transactions.size();
    }

    public AprioriResult run() {
        Map<Set<String>, Integer> frequentItemSets = new HashMap<>();
        List<Set<String>> largeItemSets = new ArrayList<>();
        List<String> associationRules = new ArrayList<>();

        List<Set<String>> candidateItemSets = getCandidateItemSets();
        while (!candidateItemSets.isEmpty()) {
            Map<Set<String>, Integer> currentFrequentItemSets = getFrequentItemSets(candidateItemSets);
            if (!currentFrequentItemSets.isEmpty()) {
                frequentItemSets.putAll(currentFrequentItemSets);
                largeItemSets.addAll(currentFrequentItemSets.keySet());
                candidateItemSets = generateNewCandidates(currentFrequentItemSets.keySet());
            } else {
                break;
            }
        }

        generateAssociationRules(frequentItemSets, associationRules);
        return new AprioriResult(largeItemSets, associationRules);
    }

    private List<Set<String>> getCandidateItemSets() {
        Set<String> items = new HashSet<>();
        for (Task task : transactions) {
            items.addAll(task.getItems());
        }

        List<Set<String>> candidateItemSets = new ArrayList<>();
        List<String> itemList = new ArrayList<>(items);
        generateCombinations(itemList, 1, 0, new HashSet<>(), candidateItemSets);
        return candidateItemSets;
    }

    private void generateCombinations(List<String> items, int size, int start, Set<String> currentSet, List<Set<String>> result) {
        if (currentSet.size() == size) {
            result.add(new HashSet<>(currentSet));
            return;
        }

        for (int i = start; i < items.size(); i++) {
            currentSet.add(items.get(i));
            generateCombinations(items, size, i + 1, currentSet, result);
            currentSet.remove(items.get(i));
        }
    }

    private Map<Set<String>, Integer> getFrequentItemSets(List<Set<String>> candidateItemSets) {
        Map<Set<String>, Integer> frequentItemSets = new HashMap<>();

        for (Set<String> candidate : candidateItemSets) {
            int count = 0;
            for (Task task : transactions) {
                if (task.getItems().containsAll(candidate)) {
                    count++;
                }
            }
            if ((double) count / totalTransactions >= minSupport) {
                frequentItemSets.put(candidate, count);
            }
        }

        return frequentItemSets;
    }

    private List<Set<String>> generateNewCandidates(Set<Set<String>> frequentItemSets) {
        List<Set<String>> newCandidates = new ArrayList<>();
        List<Set<String>> frequentList = new ArrayList<>(frequentItemSets);

        for (int i = 0; i < frequentList.size(); i++) {
            for (int j = i + 1; j < frequentList.size(); j++) {
                Set<String> candidate = new HashSet<>(frequentList.get(i));
                candidate.addAll(frequentList.get(j));

                if (candidate.size() == frequentList.get(i).size() + 1 && isValidCandidate(candidate, frequentItemSets)) {
                    newCandidates.add(candidate);
                }
            }
        }

        return newCandidates;
    }

    private boolean isValidCandidate(Set<String> candidate, Set<Set<String>> frequentItemSets) {
        for (String item : candidate) {
            Set<String> subset = new HashSet<>(candidate);
            subset.remove(item);
            if (!frequentItemSets.contains(subset)) {
                return false;
            }
        }
        return true;
    }

    private void generateAssociationRules(Map<Set<String>, Integer> frequentItemSets, List<String> associationRules) {
        for (Set<String> itemset : frequentItemSets.keySet()) {
            if (itemset.size() > 1) {
                List<Set<String>> subsets = getSubsets(itemset);
                int itemSetSupport = frequentItemSets.get(itemset);

                for (Set<String> subset : subsets) {
                    Set<String> complement = new HashSet<>(itemset);
                    complement.removeAll(subset);
                    if (!complement.isEmpty()) {
                        int subsetSupport = frequentItemSets.getOrDefault(subset, 0);
                        if (subsetSupport > 0) {
                            double confidence = (double) itemSetSupport / subsetSupport;
                            if (confidence >= minConfidence) {
                                associationRules.add(subset + " => " + complement + " (Confidence: " + confidence * 100 + "%)");
                            }
                        }
                    }
                }
            }
        }
    }

    private List<Set<String>> getSubsets(Set<String> itemset) {
        List<Set<String>> subsets = new ArrayList<>();
        generateSubsets(new ArrayList<>(itemset), 0, new HashSet<>(), subsets);
        return subsets;
    }

    private void generateSubsets(List<String> items, int index, Set<String> currentSet, List<Set<String>> subsets) {
        if (index == items.size()) {
            if (!currentSet.isEmpty()) {
                subsets.add(new HashSet<>(currentSet));
            }
            return;
        }

        generateSubsets(items, index + 1, currentSet, subsets);
        currentSet.add(items.get(index));
        generateSubsets(items, index + 1, currentSet, subsets);
        currentSet.remove(items.get(index));
    }
}