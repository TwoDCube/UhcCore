package com.gmail.val59000mc.scenarios.scenariolisteners;

import com.gmail.val59000mc.customitems.UhcItems;
import com.gmail.val59000mc.scenarios.Scenario;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DoubleGoldListener extends ScenarioListener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (isActivated(Scenario.CUTCLEAN) || isActivated(Scenario.TRIPLEORES) || isActivated(Scenario.VEINMINER)) {
            return;
        }

        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation().add(0.5, 0, 0.5);

        if (block.getType() == Material.GOLD_ORE) {
            block.setType(Material.AIR);
            loc.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 2));
            UhcItems.spawnExtraXp(loc, 6);
        }
    }

}