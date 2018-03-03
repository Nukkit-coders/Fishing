package solo.fishing;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase implements Listener{

	public static Main instance;

	public static Main getInstance(){
		return instance;
	}



	public Map<String, EntityFishingHook> fishing = new HashMap<>();

	@Override
	public void onLoad(){
		instance = this;
	}

	@Override
	public void onEnable(){
		this.getDataFolder().mkdirs();
		FishSelector.init();

		this.getServer().getPluginManager().registerEvents(this, this);
	}

	public void startFishing(Player player){
		CompoundTag nbt = new CompoundTag()
				.putList(new ListTag<DoubleTag>("Pos")
						.add(new DoubleTag("", player.x))
						.add(new DoubleTag("", player.y + player.getEyeHeight()))
						.add(new DoubleTag("", player.z)))
				.putList(new ListTag<DoubleTag>("Motion")
						.add(new DoubleTag("", -Math.sin(player.yaw / 180 + Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
						.add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
						.add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
				.putList(new ListTag<FloatTag>("Rotation")
						.add(new FloatTag("", (float) player.yaw))
						.add(new FloatTag("", (float) player.pitch)));
		double f = 0.8;
		EntityFishingHook fishingHook = new EntityFishingHook(player.chunk, nbt, player);
		fishingHook.setMotion(fishingHook.getMotion().multiply(f));
		if(player.isSurvival()){
			// TODO
		}
		ProjectileLaunchEvent ev = new ProjectileLaunchEvent(fishingHook);
		Position pos = player.getLocation();
		this.getServer().getPluginManager().callEvent(ev);
		if(ev.isCancelled()){
			fishingHook.kill();
		}else{
			fishingHook.spawnToAll();
		}

		this.fishing.put(player.getName(), fishingHook);
	}

	public void stopFishing(Player player){
		this.fishing.remove(player.getName()).reelLine();
	}

	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent event){
		if(this.fishing.containsKey(event.getPlayer().getName())){
			this.fishing.remove(event.getPlayer().getName()).kill();
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && event.getItem().getId() == Item.FISHING_ROD){
			if(this.fishing.containsKey(event.getPlayer().getName())){
				this.stopFishing(event.getPlayer());
			}else{
				this.startFishing(event.getPlayer());
				event.getItem().setDamage(5);
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		if(this.fishing.containsKey(event.getPlayer().getName())){
			this.fishing.remove(event.getPlayer().getName()).kill();
		}
	}
}
