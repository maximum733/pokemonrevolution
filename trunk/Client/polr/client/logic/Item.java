/*
 Pokemon Global. A Pokemon MMO based on the series of games made by Nintendo.
 Copyright � 2007-2008 Pokemon Global Team

 This file is part of Pokemon Global.

 Pokemon Global is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Pokemon Global is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Pokemon Global.  If not, see <http://www.gnu.org/licenses/>.
*/

package polr.client.logic;

public class Item {
	private String name;
	private int id;
	private int quantity;
	private boolean requiresPokeSelect;
	private boolean requiresMoveSlotSelect;
	
	public int getID() {
		return id;
	}
	
	public boolean pokeSelect() {
		return requiresPokeSelect;
	}
	
	public boolean moveSlotSelect() {
		return requiresMoveSlotSelect;
	}
	
	public String getName() {
		return name;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void increaseAmount() {
		quantity = quantity < 99 ? quantity + 1 : quantity;
	}
	
	public void decreaseAmount() {
		quantity = quantity > 0 ? quantity - 1 : quantity;
	}
	
	//Checks item requirements and lists items
	public Item(int id) {
		this.id = id;
		quantity = 1;
		if(id < 5) {
			requiresPokeSelect = false;
			requiresMoveSlotSelect = false;
		} else if(id >= 5 && id < 20) {
			requiresPokeSelect = true;
			requiresMoveSlotSelect = false;
		} else if(id >= 20 && id < 113) {
			requiresPokeSelect = true;
			requiresMoveSlotSelect = true;
		} else {
			requiresPokeSelect = true;
			requiresMoveSlotSelect = false;
		}
		switch(id) {
		case 1:
			name = "Master Ball";
			break;
		case 2:
			name = "Ultra Ball";
			break;
		case 3:
			name = "Great Ball";
			break;
		case 4:
			name = "Poke Ball";
			break;
		case 5:
			name = "Potion";
			break;
		case 6:
			name = "Antidote";
			break;
		case 7:
			name = "Burn Heal";
			break;
		case 8:
			name = "Ice Heal";
			break;
		case 9:
			name = "Awakening";
			break;
		case 10:
			name = "Paralyz Heal";
			break;
		case 11:
			name = "Full Restore";
			break;
		case 12:
			name = "Max Potion";
			break;
		case 13:
			name = "Hyper Potion";
			break;
		case 14:
			name = "Super Potion";
			break;
		case 15:
			name = "Full Heal";
			break;
		case 16:
			name = "Revive";
			break;
		case 17:
			name = "Max Revive";
			break;
		case 18:
			name = "Rare Candy";
			break;
		case 19:
			name = "Lava Cookie";
			break;
		case 20:
			name = "TM01 Focus Punch";
			break;
		case 21:
			name = "TM02 Dragon Claw";
			break;
		case 22:
			name = "TM03 Water Pulse";
			break;
		case 23:
			name = "TM04 Calm Mind";
			break;
		case 24:
			name = "TM05 Roar";
			break;
		case 25:
			name = "TM06 Toxic";
			break;
		case 26:
			name = "TM07 Hail";
			break;
		case 27:
			name = "TM08 Bulk Up";
			break;
		case 28:
			name = "TM09 Bullet Seed";
			break;
		case 29:
			name = "TM10 Hidden Power";
			break;
		case 30:
			name = "TM11 Sunny Day";
			break;
		case 31:
			name = "TM12 Taunt";
			break;
		case 32:
			name = "TM13 Ice Beam";
			break;
		case 33:
			name = "TM14 Blizzard";
			break;
		case 34:
			name = "TM15 Hyper Beam";
			break;
		case 35:
			name = "TM16 Light Screen";
			break;
		case 36:
			name = "TM17 Protect";
			break;
		case 37:
			name = "TM18 Rain Dance";
			break;
		case 38:
			name = "TM19 Giga Drain";
			break;
		case 39:
			name = "TM20 Safeguard";
			break;
		case 40:
			name = "TM21 Frustration";
			break;
		case 41:
			name = "TM22 SolarBeam";
			break;
		case 42:
			name = "TM23 Iron Tail";
			break;
		case 43:
			name = "TM24 Thunderbolt";
			break;
		case 44:
			name = "TM25 Thunder";
			break;
		case 45:
			name = "TM26 Earthquake";
			break;
		case 46:
			name = "TM27 Return";
			break;
		case 47:
			name = "TM28 Dig";
			break;
		case 48:
			name = "TM29 Psychic";
			break;
		case 49:
			name = "TM30 Shadow Ball";
			break;
		case 50:
			name = "TM31 Brick Break";
			break;
		case 51:
			name = "TM32 Double Team";
			break;
		case 52:
			name = "TM33 Reflect";
			break;
		case 53:
			name = "TM34 Shock Wave";
			break;
		case 54:
			name = "TM35 Flamethrower";
			break;
		case 55:
			name = "TM36 Sludge Bomb";
			break;
		case 56:
			name = "TM37 Sandstorm";
			break;
		case 57:
			name = "TM38 Fire Blast";
			break;
		case 58:
			name = "TM39 Rock Tomb";
			break;
		case 59:
			name = "TM40 Aerial Ace";
			break;
		case 60:
			name = "TM41 Torment";
			break;
		case 61:
			name = "TM42 Facade";
			break;
		case 62:
			name = "TM43 Secret Power";
			break;
		case 63:
			name = "TM44 Rest";
			break;
		case 64:
			name = "TM45 Attract";
			break;
		case 65:
			name = "TM46 Thief";
			break;
		case 66:
			name = "TM47 Steel Wing";
			break;
		case 67:
			name = "TM48 Skill Swap";
			break;
		case 68:
			name = "TM49 Snatch";
			break;
		case 69:
			name = "TM50 Overheat";
			break;
		case 70:
			name = "TM51 Roost";
			break;
		case 71:
			name = "TM52 Focus Blast";
			break;
		case 72:
			name = "TM53 Energy Ball";
			break;
		case 73:
			name = "TM54 False Swipe";
			break;
		case 74:
			name = "TM55 Brine";
			break;
		case 75:
			name = "TM56 Fling";
			break;
		case 76:
			name = "TM57 Charge Beam";
			break;
		case 77:
			name = "TM58 Endure";
			break;
		case 78:
			name = "TM59 Dragon Pulse";
			break;
		case 79:
			name = "TM60 Drain Punch";
			break;
		case 80:
			name = "TM61 Will-O-Wisp";
			break;
		case 81:
			name = "TM62 Silver Wind";
			break;
		case 82:
			name = "TM63 Embargo";
			break;
		case 83:
			name = "TM64 Explosion";
			break;
		case 84:
			name = "TM65 Shadow Claw";
			break;
		case 85:
			name = "TM66 Payback";
			break;
		case 86:
			name = "TM67 Recycle";
			break;
		case 87:
			name = "TM68 Giga Impact";
			break;
		case 88:
			name = "TM69 Rock Polish";
			break;
		case 89:
			name = "TM70 Flash";
			break;
		case 90:
			name = "TM71 Stone Edge";
			break;
		case 91:
			name = "TM72 Avalanche";
			break;
		case 92:
			name = "TM73 Thunder Wave";
			break;
		case 93:
			name = "TM74 Gyro Ball";
			break;
		case 94:
			name = "TM75 Swords Dance";
			break;
		case 95:
			name = "TM76 Stealth Rock";
			break;
		case 96:
			name = "TM77 Psych Up";
			break;
		case 97:
			name = "TM78 Captivate";
			break;
		case 98:
			name = "TM79 Dark Pulse";
			break;
		case 99:
			name = "TM80 Rock Slide";
			break;
		case 100:
			name = "TM81 X-Scissor";
			break;
		case 101:
			name = "TM82 Sleep Talk";
			break;
		case 102:
			name = "TM83 Natural Gift";
			break;
		case 103:
			name = "TM84 Poison Jab";
			break;
		case 104:
			name = "TM85 Dream Eater";
			break;
		case 105:
			name = "TM86 Grass Knot";
			break;
		case 106:
			name = "TM87 Swagger";
			break;
		case 107:
			name = "TM88 Pluck";
			break;
		case 108:
			name = "TM89 U-turn";
			break;
		case 109:
			name = "TM90 Substitute";
			break;
		case 110:
			name = "TM91 Flash Cannon";
			break;
		case 111:
			name = "TM92 Trick Room";
			break;
		case 112:
			name = "Fire Stone";
			break;
		case 113:
			name = "Water Stone";
			break;
		case 114:
			name = "Thunder Stone";
			break;
		case 115:
			name = "Leaf Stone";
			break;
		case 116:
			name = "Moon Stone";
			break;
		case 117:
			name = "Sun Stone";
			break;
		case 118:
			name = "Shiny Stone";
			break;
		case 119:
			name = "Dusk Stone";
			break;
		case 120:
			name = "Dawn Stone";
			break;
		default:
			name = "!Error!";
			id = 0;
			quantity = 0;
			break;
		}
	}
	
	public static String getName(int id) {
		String name = "";
		switch(id) {
		case 1:
			name = "Master Ball";
			break;
		case 2:
			name = "Ultra Ball";
			break;
		case 3:
			name = "Great Ball";
			break;
		case 4:
			name = "Poke Ball";
			break;
		case 5:
			name = "Potion";
			break;
		case 6:
			name = "Antidote";
			break;
		case 7:
			name = "Burn Heal";
			break;
		case 8:
			name = "Ice Heal";
			break;
		case 9:
			name = "Awakening";
			break;
		case 10:
			name = "Paralyz Heal";
			break;
		case 11:
			name = "Full Restore";
			break;
		case 12:
			name = "Max Potion";
			break;
		case 13:
			name = "Hyper Potion";
			break;
		case 14:
			name = "Super Potion";
			break;
		case 15:
			name = "Full Heal";
			break;
		case 16:
			name = "Revive";
			break;
		case 17:
			name = "Max Revive";
			break;
		case 18:
			name = "Rare Candy";
			break;
		case 19:
			name = "Lava Cookie";
			break;
		case 20:
			name = "TM01 Focus Punch";
			break;
		case 21:
			name = "TM02 Dragon Claw";
			break;
		case 22:
			name = "TM03 Water Pulse";
			break;
		case 23:
			name = "TM04 Calm Mind";
			break;
		case 24:
			name = "TM05 Roar";
			break;
		case 25:
			name = "TM06 Toxic";
			break;
		case 26:
			name = "TM07 Hail";
			break;
		case 27:
			name = "TM08 Bulk Up";
			break;
		case 28:
			name = "TM09 Bullet Seed";
			break;
		case 29:
			name = "TM10 Hidden Power";
			break;
		case 30:
			name = "TM11 Sunny Day";
			break;
		case 31:
			name = "TM12 Taunt";
			break;
		case 32:
			name = "TM13 Ice Beam";
			break;
		case 33:
			name = "TM14 Blizzard";
			break;
		case 34:
			name = "TM15 Hyper Beam";
			break;
		case 35:
			name = "TM16 Light Screen";
			break;
		case 36:
			name = "TM17 Protect";
			break;
		case 37:
			name = "TM18 Rain Dance";
			break;
		case 38:
			name = "TM19 Giga Drain";
			break;
		case 39:
			name = "TM20 Safeguard";
			break;
		case 40:
			name = "TM21 Frustration";
			break;
		case 41:
			name = "TM22 SolarBeam";
			break;
		case 42:
			name = "TM23 Iron Tail";
			break;
		case 43:
			name = "TM24 Thunderbolt";
			break;
		case 44:
			name = "TM25 Thunder";
			break;
		case 45:
			name = "TM26 Earthquake";
			break;
		case 46:
			name = "TM27 Return";
			break;
		case 47:
			name = "TM28 Dig";
			break;
		case 48:
			name = "TM29 Psychic";
			break;
		case 49:
			name = "TM30 Shadow Ball";
			break;
		case 50:
			name = "TM31 Brick Break";
			break;
		case 51:
			name = "TM32 Double Team";
			break;
		case 52:
			name = "TM33 Reflect";
			break;
		case 53:
			name = "TM34 Shock Wave";
			break;
		case 54:
			name = "TM35 Flamethrower";
			break;
		case 55:
			name = "TM36 Sludge Bomb";
			break;
		case 56:
			name = "TM37 Sandstorm";
			break;
		case 57:
			name = "TM38 Fire Blast";
			break;
		case 58:
			name = "TM39 Rock Tomb";
			break;
		case 59:
			name = "TM40 Aerial Ace";
			break;
		case 60:
			name = "TM41 Torment";
			break;
		case 61:
			name = "TM42 Facade";
			break;
		case 62:
			name = "TM43 Secret Power";
			break;
		case 63:
			name = "TM44 Rest";
			break;
		case 64:
			name = "TM45 Attract";
			break;
		case 65:
			name = "TM46 Thief";
			break;
		case 66:
			name = "TM47 Steel Wing";
			break;
		case 67:
			name = "TM48 Skill Swap";
			break;
		case 68:
			name = "TM49 Snatch";
			break;
		case 69:
			name = "TM50 Overheat";
			break;
		case 70:
			name = "TM51 Roost";
			break;
		case 71:
			name = "TM52 Focus Blast";
			break;
		case 72:
			name = "TM53 Energy Ball";
			break;
		case 73:
			name = "TM54 False Swipe";
			break;
		case 74:
			name = "TM55 Brine";
			break;
		case 75:
			name = "TM56 Fling";
			break;
		case 76:
			name = "TM57 Charge Beam";
			break;
		case 77:
			name = "TM58 Endure";
			break;
		case 78:
			name = "TM59 Dragon Pulse";
			break;
		case 79:
			name = "TM60 Drain Punch";
			break;
		case 80:
			name = "TM61 Will-O-Wisp";
			break;
		case 81:
			name = "TM62 Silver Wind";
			break;
		case 82:
			name = "TM63 Embargo";
			break;
		case 83:
			name = "TM64 Explosion";
			break;
		case 84:
			name = "TM65 Shadow Claw";
			break;
		case 85:
			name = "TM66 Payback";
			break;
		case 86:
			name = "TM67 Recycle";
			break;
		case 87:
			name = "TM68 Giga Impact";
			break;
		case 88:
			name = "TM69 Rock Polish";
			break;
		case 89:
			name = "TM70 Flash";
			break;
		case 90:
			name = "TM71 Stone Edge";
			break;
		case 91:
			name = "TM72 Avalanche";
			break;
		case 92:
			name = "TM73 Thunder Wave";
			break;
		case 93:
			name = "TM74 Gyro Ball";
			break;
		case 94:
			name = "TM75 Swords Dance";
			break;
		case 95:
			name = "TM76 Stealth Rock";
			break;
		case 96:
			name = "TM77 Psych Up";
			break;
		case 97:
			name = "TM78 Captivate";
			break;
		case 98:
			name = "TM79 Dark Pulse";
			break;
		case 99:
			name = "TM80 Rock Slide";
			break;
		case 100:
			name = "TM81 X-Scissor";
			break;
		case 101:
			name = "TM82 Sleep Talk";
			break;
		case 102:
			name = "TM83 Natural Gift";
			break;
		case 103:
			name = "TM84 Poison Jab";
			break;
		case 104:
			name = "TM85 Dream Eater";
			break;
		case 105:
			name = "TM86 Grass Knot";
			break;
		case 106:
			name = "TM87 Swagger";
			break;
		case 107:
			name = "TM88 Pluck";
			break;
		case 108:
			name = "TM89 U-turn";
			break;
		case 109:
			name = "TM90 Substitute";
			break;
		case 110:
			name = "TM91 Flash Cannon";
			break;
		case 111:
			name = "TM92 Trick Room";
			break;
		case 112:
			name = "Fire Stone";
			break;
		case 113:
			name = "Water Stone";
			break;
		case 114:
			name = "Thunder Stone";
			break;
		case 115:
			name = "Leaf Stone";
			break;
		case 116:
			name = "Moon Stone";
			break;
		case 117:
			name = "Sun Stone";
			break;
		case 118:
			name = "Shiny Stone";
			break;
		case 119:
			name = "Dusk Stone";
			break;
		case 120:
			name = "Dawn Stone";
			break;
		default:
			name = "";
			break;
		}
		return name;
	}
}
