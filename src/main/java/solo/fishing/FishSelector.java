package solo.fishing;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class FishSelector {

	private FishSelector() {
	}

	public static int hollRate;
	public static Map<String, Integer> fishes;
	public static Map<String, Integer> exps;

	@SuppressWarnings({ "deprecation", "serial", "unchecked" })
	public static void init() {
		Config config = new Config(new File(Main.getInstance().getDataFolder(), "setting.yml"), Config.YAML, new LinkedHashMap<String, Object>(){{
			put("fishes", new LinkedHashMap<String, Integer>(){{
				put("349:0", 340);
				put("349:1", 120);
				put("349:2", 80);
				put("349:3", 50);
				put("346:?", 10);
				put("301:?", 10);
				put("367:0", 20);
				put("280:0", 15);
			}});
			put("exps", new LinkedHashMap<String, Integer>(){{
				put("349:0", 50);
				put("349:1", 80);
				put("349:2", 90);
				put("349:3", 100);
				put("346:?", 30);
			}});
		}});

		fishes = (LinkedHashMap<String, Integer>) config.get("fishes");
		exps = (LinkedHashMap<String, Integer>) config.get("exps");

		hollRate = 0;
		for (int rate : fishes.values()) {
			hollRate += rate;
		}
	}

	public static String select() {
		Random random = new Random();
		int rand = random.nextInt(hollRate);
		int current = 0;
		for (Map.Entry<String, Integer> entry : fishes.entrySet()) {
			if (current > rand) {
				return entry.getKey();
			} else {
				current += entry.getValue();
			}
		}
		return "";
	}

	public static Item getFish(String code) {
		Random random = new Random();
		int id = Integer.parseInt(code.split(":")[0]);
		Item item = Item.get(id);
		if (code.split(":")[1].equals("?") && item instanceof ItemTool) {
			ItemTool tool = (ItemTool) item;
			tool.setDamage(random.nextInt(tool.getMaxDurability() - 20) + 20);
			return tool;
		}
		return item;
	}

	public static int getExperience(String code) {
		if (exps.containsKey(code)) {
			return exps.get(code);
		}
		return 0;
	}
}
