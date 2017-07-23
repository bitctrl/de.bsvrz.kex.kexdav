/*
 * Copyright 2017 by Kappich Systemberatung Aachen
 * 
 * This file is part of de.bsvrz.kex.kexdav.
 * 
 * de.bsvrz.kex.kexdav is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * de.bsvrz.kex.kexdav is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with de.bsvrz.kex.kexdav.  If not, see <http://www.gnu.org/licenses/>.

 * Contact Information:
 * Kappich Systemberatung
 * Martin-Luther-Straße 14
 * 52062 Aachen, Germany
 * phone: +49 241 4090 436 
 * mail: <info@kappich.de>
 */

package de.bsvrz.kex.kexdav.dataplugin;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.kex.kexdav.correspondingObjects.ObjectManagerInterface;
import de.bsvrz.kex.kexdav.dataexchange.DataCopyException;
import de.bsvrz.kex.kexdav.management.ManagerInterface;
import de.bsvrz.kex.kexdav.systemobjects.KExDaVAttributeGroupData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plugin, das vor dem Übertragen der Daten alle booleen-Flags im zu übertragenen Datum auf "Nein" setzt um undefinierte Daten bei Umfelddaten zu vermeiden.
 *
 * @author Kappich Systemberatung
 */
@SuppressWarnings("unused")
public class UfdDataPlugin implements ExternalKExDaVDataPlugin {

	/**
	 * Betrachtete ATGs
	 */
	private static final List<String> ATGS = Arrays.asList(
			"atg.ufdsABewerteterSchallDruckPegel",
			"atg.ufdsBasisPegel",
			"atg.ufdsEnergieAequivalenterDauerSchallPegel",
			"atg.ufdsFahrBahnFeuchte",
			"atg.ufdsFahrBahnGlätte",
			"atg.ufdsFahrBahnOberFlächenTemperatur",
			"atg.ufdsFahrBahnOberFlächenZustand",
			"atg.ufdsGefrierTemperatur",
			"atg.ufdsGriffigkeit",
			"atg.ufdsHelligkeit",
			"atg.ufdsLuftDruck",
			"atg.ufdsLuftTemperatur",
			"atg.ufdsMittlererSpitzenPegel",
			"atg.ufdsNiederschlagsArt",
			"atg.ufdsNiederschlagsIntensität",
			"atg.ufdsNiederschlagsMenge",
			"atg.ufdsRelativeLuftFeuchte",
			"atg.ufdsRestSalz",
			"atg.ufdsSchneeFilmDicke",
			"atg.ufdsSchneeHöhe",
			"atg.ufdsSichtWeite",
			"atg.ufdsStickoxideNOx",
			"atg.ufdsTaupunktTemperatur",
			"atg.ufdsTemperaturInTiefe1",
			"atg.ufdsTemperaturInTiefe2",
			"atg.ufdsTemperaturInTiefe3",
			"atg.ufdsWasserFilmDicke",
			"atg.ufdsWindGeschwindigkeitMittelWert",
			"atg.ufdsWindGeschwindigkeitSpitzenWert",
			"atg.ufdsWindRichtung"
	);

	@Override
	public Collection<AttributeGroupPair> getAttributeGroupPairs() {
		return ATGS.stream().map(it -> new AttributeGroupPair(it, it)).collect(Collectors.toList());
	}

	@Override
	public void process(final KExDaVAttributeGroupData input, final KExDaVAttributeGroupData output, final ObjectManagerInterface objectManager, final ManagerInterface manager) throws DataCopyException {
		// Zuerst Booleans auf nein setzen
		resetBooleans(output);
		
		// Alle sonstigen übertragbaren Daten übertragen
		BasicKExDaVDataPlugin.copyDataTree(input, output, objectManager, manager);
	}

	/**
	 * Setzt alle booleans rekursiv auf "Nein"
	 * @param data Datum
	 */
	private static void resetBooleans(final Data data) {
		if(data.isPlain()){
			if(data.getAttributeType().getPid().equals("att.jaNein")){
				data.asTextValue().setText("Nein");
			}
		}
		else {
			for(Data inner : data) {
				resetBooleans(inner);
			}
		}
	}
}
