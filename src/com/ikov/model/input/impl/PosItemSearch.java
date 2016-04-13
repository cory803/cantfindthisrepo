package com.ikov.model.input.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.ikov.model.actions.ActionHandler;
import com.ikov.model.actions.ButtonAction;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.Input;
import com.ikov.world.content.pos.PlayerOwnedShops;
import com.ikov.world.content.pos.PosDetails;
import com.ikov.world.content.pos.PosOffer;
import com.ikov.world.content.pos.PosOffers;
import com.ikov.world.entity.impl.player.Player;

public class PosItemSearch extends Input {

  private static Map<PosDetails, PosOffer> foundOffers = new HashMap<PosDetails, PosOffer>();

  @Override
  public void handleSyntax(Player player, String syntax) {
    if (syntax.length() <= 1) {
      player.getPacketSender().sendMessage("Invalid syntax entered.");
      return;
    }
    int start_button = -24062;
    int start_owner_name = 41468;
    int start_caption = 41469;
    int index = 0;

    String itemName = syntax;
    if (itemName.length() < 3) {
      player.getPacketSender().sendMessage("Your search must contain atleast 3 characters.");
      return;
    }
    reset();
    for (PosOffers o : PlayerOwnedShops.SHOPS) {
      if (o == null)
        continue;

      for (int q = 0; q < o.getOffers().size(); q++) {
        if (o.getOffers().get(q) != null && !o.getOwner().equalsIgnoreCase(player.getUsername())) {
          ItemDefinition def = ItemDefinition.forId(o.getOffers().get(q).getItemId());
          if (def != null && def.getName().toLowerCase().contains(itemName)) {
            foundOffers.put(new PosDetails(start_button, o.getOwner(), o.getCaption()),
                new PosOffer(o.getOffers().get(q).getItemId(), o.getOffers().get(q).getAmount(),
                    o.getOffers().get(q).getSoldAmount(), o.getOffers().get(q).getPrice()));
            start_button += 4;
          }
        }
      }
    }

    if (foundOffers.size() < 1) {
      player.getPacketSender().sendMessage("Your search did not return any offers.");
      return;
    }

    foundOffers = sortByValue(foundOffers);

    for (Map.Entry<PosDetails, PosOffer> entry : foundOffers.entrySet()) {
      if (index >= 100) {
        break;
      }
      PosDetails pd = entry.getKey();
      PosOffer p = entry.getValue();
      String item_name = ItemDefinition.forId(p.getItemId()).getName();
      start_caption += 4;
      start_owner_name += 4;
      pd.setButtonId(-24062 + (4 * index));
      player.getPacketSender().sendString(start_owner_name, pd.getOwner());
      player.getPacketSender().sendString(start_caption,
          "Found: " + item_name + " for " + formatAmount(p.getPrice()));
      index++;
    }

    for (int i = start_owner_name + 4; i < 41868; i++)
      player.getPacketSender().sendString(i, "");

    for (int i = start_caption + 4; i < 41869; i++)
      player.getPacketSender().sendString(i, "");

  }

  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
    Map<K, V> result = new LinkedHashMap<>();
    Stream<Entry<K, V>> st = map.entrySet().stream();

    st.sorted(Comparator.comparing(e -> e.getValue()))
        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

    return result;
  }

  public final String formatAmount(long amount) {
    String format = "Too high!";
    if (amount >= 0 && amount < 100000) {
      format = String.valueOf(amount);
    } else if (amount >= 100000 && amount < 1000000) {
      format = amount / 1000 + "K";
    } else if (amount >= 1000000 && amount < 1000000000L) {
      format = amount / 1000000 + "M";
    } else if (amount >= 1000000000L && amount < 1000000000000L) {
      format = amount / 1000000000 + "B";
    } else if (amount >= 10000000000000L && amount < 10000000000000000L) {
      format = amount / 1000000000000L + "T";
    } else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
      format = amount / 1000000000000000L + "QD";
    } else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
      format = amount / 1000000000000000000L + "QT";
    }
    return format;
  }

  public static PosDetails forId(int i) {
    for (Map.Entry<PosDetails, PosOffer> map : foundOffers.entrySet()) {
      PosDetails pd = map.getKey();
      if (pd.getButtonId() == i)
        return pd;
    }
    return null;
  }

  public static void reset() {
    foundOffers.clear();
  }

  static {
    for (int i = -24062; i < -23666; i += 4) {
      final int buttonId = i;
      ActionHandler.getActionHandler().submit(i, new ButtonAction() {

        @Override
        public void handle(Player player) {
          if (foundOffers.size() > 0) {
            PosDetails pd = forId(buttonId);
            if (pd != null) {
              PlayerOwnedShops.openShop(pd.getOwner(), player);
            }
          }
        }

      });
    }
  }

}
