package solo.fishing;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

/**
 * @author CreeperFace
 */
public class PlayerFishEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final EntityFishingHook hook;
    private final Item item;
    private final int experience;

    public PlayerFishEvent(Player player, EntityFishingHook hook, Item item, int experience) {
        this.player = player;
        this.hook = hook;
        this.item = item;
        this.experience = experience;
    }

    public EntityFishingHook getHook() {
        return hook;
    }

    public int getExperience() {
        return experience;
    }

    public Item getItem() {
        return item;
    }
}
