package solo.fishing;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class FishSelector {

	public static int hollRate;
	public static Map<String, Integer> fishes;
	public static Map<String, Integer> exps;

	@SuppressWarnings({"deprecation", "serial", "unchecked"})
	public static void init() {
		Config config = new Config(new File(Main.getInstance().getDataFolder(), "settings.yml"), Config.YAML, new LinkedHashMap<String, Object>() {{
			put("fishes", new LinkedHashMap<String, Integer>() {{
				put("349:0", 90);
				put("460:0", 80);
				put("461:0", 60);
				put("462:0", 40);
				put("346:?", 10);
				put("301:?", 10);
				put("367:0", 20);
				put("280:0", 15);
			}});
			put("exps", new LinkedHashMap<String, Integer>() {{
				put("349:0", 10);
				put("460:0", 10);
				put("461:0", 10);
				put("462:0", 10);
				put("346:?", 10);
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

		return "349:0";
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
