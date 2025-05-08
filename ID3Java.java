import java.util.*;


public class ID3Java {
	private static class Node {
		String attribute;
		Map<String, Node> child;
		String label;
		Node(String attribute) {
			this.attribute = attribute;
			child = new HashMap<>();
			label = null;
		}
	}
	/* kiểm tra và lấy giá trị label nếu chúng trùng nhau*/
	private static String checkDuplicateValue(List<Map<String, String>> array) {
		String check = array.get(0).get("decision");
		for (Map<String, String> instance : array) {
			if (!instance.get("decision").equals(check)) {
				return null;
			}
		}
		return check;
	}
	/*tạo cây*/
	private static Node buildDecisionTree(List<Map<String, String>> array, List<String> attributes) {
		Node root = new Node(null);
		String label = checkDuplicateValue(array);
		if (label != null) {
			root.label = label;
			return root;
		}
		String choosenAttribute = getChoosenAttribute(array, attributes);
		root.attribute = choosenAttribute;
		List<String> diffAttr = getDifferentAttribute(array, choosenAttribute);
		for (String each : diffAttr) {
			List<Map<String, String>> newArray = createArray(array, choosenAttribute, each);
			List<String> newAttributes = new ArrayList<>(attributes);
			newAttributes.remove(choosenAttribute);
			Node child = buildDecisionTree(newArray, newAttributes);
			root.child.put(each, child);
		}
		return root;
	}
	/* tìm ra thuộc tính được chọn bằng entropy nhỏ nhất*/
	private static String getChoosenAttribute(List<Map<String, String>> array, List<String> attributes) {
		double choosenEntropy = Double.POSITIVE_INFINITY;
		String choosenAttribute = null;
		for (String attribute : attributes) {
			double entropy = calcEntropy(array, attribute);
			System.out.println(attribute+": " + entropy);
			if (entropy < choosenEntropy) {
				choosenEntropy = entropy;
				choosenAttribute = attribute;
			}
		}
		System.out.println("chọn "+ choosenAttribute);
		System.out.println("---------------");
		return choosenAttribute;
	}
	/*tính entropy*/
	private static double calcEntropy(List<Map<String, String>> array, String attribute) {
		List<String> diffAttr = getDifferentAttribute(array, attribute);
		double entropy = 0.0;
		for (String each : diffAttr) {
			List<Map<String, String>> newArray = createArray(array, attribute, each);
			double value = (double) newArray.size() / array.size();
			Map<String, Integer> countLabel = new HashMap<>();
			for (Map<String, String> instance : newArray) {
				String label = instance.get("decision");
				countLabel.put(label, countLabel.getOrDefault(label, 0) + 1);
			}
			double tmp = 0.0;
			for (int count : countLabel.values()) {
				double value1 = (double) count / newArray.size();
				tmp -= value1 * Math.log(value1) / Math.log(2);
			}
			entropy += value * tmp;
		}
		return entropy;
	}

	/* tìm các giá trị khác nhau trong thuộc tính*/
	private static List<String> getDifferentAttribute(List<Map<String, String>> array, String attribute) {
		Set<String> value = new HashSet<>();
		for (Map<String, String> each : array) {
			value.add(each.get(attribute));
		}
		return new ArrayList<>(value);
	}
	/* tạo mảng mới với thuộc tính và giá trị được chọn */
	private static List<Map<String, String>> createArray(List<Map<String, String>> array, String attribute, String value) {
		List<Map<String, String>> newArray = new ArrayList<>();
		for (Map<String, String> instance : array) {
			if (instance.get(attribute).equals(value)) {
				newArray.add(instance);
			}
		}
		return newArray;
	}
	
	/*in cây*/
	private static void printDecisionTree(Node node) {
		if (node.label != null) {
			System.out.println(" ------" + node.label);
		} else {
			System.out.println("Choose: " +node.attribute );
			for (Map.Entry<String, Node> each : node.child.entrySet()) {
				System.out.println(" --" + each.getKey());
				printDecisionTree(each.getValue());
			}
		}
	}
	/*in luật*/
	private static void printLaw(Node node, String rule) {
		if(node.label != null) {
			System.out.println(rule +" => "+ node.label);
		}
		else {
			String attr = node.attribute;
			for (Map.Entry<String, Node> each : node.child.entrySet()) {
				String newRule = rule.isEmpty()?attr + " = " + each.getKey():rule + " AND " + attr + " = " + each.getKey() ;
				printLaw(each.getValue(), newRule);
			}
		}
	}

	public static void main(String[] args) {
		/*mảng dữ liệu 1*/
		List<Map<String, String>> array = new ArrayList<>();
		array.add(Map.of("color", "yellow", "height", "average", "weight", "light", "usecream", "no", "decision", "yes"));
		array.add(Map.of("color", "yellow", "height", "tall", "weight", "medium", "usecream", "yes", "decision", "no"));
		array.add(Map.of("color", "brown", "height", "short", "weight", "medium", "usecream", "yes", "decision", "no"));
		array.add(Map.of("color", "yellow", "height", "short", "weight", "medium", "usecream", "no", "decision", "yes"));
		array.add(Map.of("color", "red", "height", "average", "weight", "heavy", "usecream", "no", "decision", "yes"));
		array.add(Map.of("color", "brown", "height", "tall", "weight", "heavy", "usecream", "no", "decision", "no"));
		array.add(Map.of("color", "brown", "height", "average", "weight", "heavy", "usecream", "no", "decision", "no"));
		array.add(Map.of("color", "yellow", "height", "short", "weight", "light", "usecream", "yes", "decision", "no"));
		List<String> attributes = new ArrayList<>(Arrays.asList("color", "height", "weight", "usecream"));
		/*mảng dữ liệu 2*/
//		List<Map<String, String>> array = new ArrayList<>();
//		array.add(Map.of("weather", "rain", "leaf", "fall", "tempurare", "low", "decision", "winter"));
//		array.add(Map.of("weather", "sunny", "leaf", "green", "tempurare", "average", "decision", "spring"));
//		array.add(Map.of("weather", "sunny", "leaf", "yellow", "tempurare", "average", "decision", "autumn"));
//		array.add(Map.of("weather", "sunny", "leaf", "green", "tempurare", "high", "decision", "summer"));
//		array.add(Map.of("weather", "sunny", "leaf", "fall", "tempurare", "low", "decision", "winter"));
//		array.add(Map.of("weather", "snow", "leaf", "yellow", "tempurare", "low", "decision", "winter"));
//		array.add(Map.of("weather", "rain", "leaf", "fall", "tempurare", "average", "decision", "autumn"));
//		array.add(Map.of("weather", "rain", "leaf", "green", "tempurare", "high", "decision", "summer"));
//		array.add(Map.of("weather", "snow", "leaf", "green", "tempurare", "low", "decision", "winter"));
//		array.add(Map.of("weather", "snow", "leaf", "fall", "tempurare", "low", "decision", "winter"));
//		array.add(Map.of("weather", "rain", "leaf", "yellow", "tempurare", "average", "decision", "autumn"));
//		array.add(Map.of("weather", "rain", "leaf", "green", "tempurare", "average", "decision", "spring"));
//		List<String> attributes = new ArrayList<>(Arrays.asList("weather", "leaf", "tempurare"));
		Node root = buildDecisionTree(array, attributes);
		printDecisionTree(root);
		printLaw(root, "");
	}
}


