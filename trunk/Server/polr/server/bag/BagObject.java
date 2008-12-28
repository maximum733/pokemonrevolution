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

package polr.server.bag;

import org.simpleframework.xml.Element;

public class BagObject {
	@Element
	private int id;
	@Element
	private int quantity;
	
	/* Used only for serialization */
	public BagObject(){ }
	
	/* Used for all other instances of BagObject */
	public BagObject(int id, int q) {
		this.id = id;
		quantity = q;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getQuantityValue() {
		if(quantity < 10)
			return "0" + quantity;
		else
			return "" + quantity;
	}
	
	public void decreaseQuantity(int amount) {
		quantity = quantity - amount >= 0 ? quantity - amount:0;
	}
	
	public void addQuantity(int amount) {
		if(quantity < 99)
			quantity = quantity + amount;
	}
	
	public int getId() {
		return id;
	}
	
	public String getIdValue() {
		if(id < 10)
			return "00" + id;
		else if(id < 100)
			return "0" + id;
		else
			return "" + id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof BagObject) {
			BagObject b = (BagObject) o;
			if(id == b.getId())
				return true;
			else
				return false;
		}
		return false;
	}
}

/*
 * 000	null
001 	Master Ball 	
002 	Ultra Ball 	
003 	Great Ball 	
004 	Pok� Ball 	
005 	Potion 	
006 	Antidote 	
007 	Burn Heal 	
008 	Ice Heal 	
009 	Awakening 	
010 	Parlyz Heal 	
011 	Full Restore 	
012 	Max Potion 	
013 	Hyper Potion 	
014 	Super Potion 	
015 	Full Heal 	
016 	Revive 	
017 	Max Revive 	
018 	Ether 	
019 	Max Ether 	
020 	Elixir 	
021 	Max Elixir 	
022 	Sacred Ash 	
023 	Shoal Salt 	
024 	Shoal Shell 	
025 	Red Shard 	
026 	Blue Shard 	
027 	Yellow Shard 	
028 	Green Shard
029 	HP Up 	
030 	Protein 	
031 	Iron 	
032 	Carbos 	
033 	Calcium 	
034 	Rare Candy 	
035 	PP Up 	
036 	Zinc 	
037 	PP Max 		
038 	X Attack 	
039 	X Defend 	
040 	X Speed 	
041 	X Accuracy 	
042 	X Special 	
043 	Super Repel 	
044 	Max Repel 		
046 	Repel 		
047 	Sun Stone 	
048 	Moon Stone 	
049 	Fire Stone 	
050 	Thunderstone 	
051 	Water Stone 	
052 	Leaf Stone 	
053 	TM01 	
054 	TM02 	
055 	TM03 	
056 	TM04 	
057 	TM05 	
058 	TM06 	
059 	TM07 	
060 	TM08 	
061 	TM09 	
062 	TM10 	
063 	TM11 	
064 	TM12 	
065 	TM13 	
066 	TM14 	
067 	TM15 	
068 	TM16 	
069 	TM17 	
070 	TM18 	
071 	TM19 	
072 	TM20 	
073 	TM21 	
074 	TM22 	
075 	TM23 	
076 	TM24 	
077 	TM25 	
078 	TM26 	
079 	TM27 	
080 	TM28 	
081 	TM29 	
082 	TM30 	
083 	TM31 	
084 	TM32 	
085 	TM33 	
086 	TM34 	
087 	TM35 	
088 	TM36 	
089 	TM37 	
090 	TM38 	
091 	TM39 	
092 	TM40 	
093 	TM41 	
094 	TM42 	
095 	TM43 	
096 	TM44 	
097 	TM45 	
098 	TM46 	
099 	TM47 	
100 	TM48 	
101 	TM49 	
102 	TM50
*/
