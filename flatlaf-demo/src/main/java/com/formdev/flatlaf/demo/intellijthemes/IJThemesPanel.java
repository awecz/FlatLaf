/*
 * Copyright 2019 FormDev Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.formdev.flatlaf.demo.intellijthemes;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.IntelliJTheme;
import net.miginfocom.swing.*;

/**
 * @author Karl Tauber
 */
public class IJThemesPanel
	extends JPanel
{
	private final IJThemesManager themesManager = new IJThemesManager();
	private final PropertyChangeListener lafListener = this::lafChanged;

	public IJThemesPanel() {
		initComponents();

		// load theme infos
		themesManager.loadBundledThemes();

		// sort themes by name
		List<IJThemeInfo> themes = new ArrayList<>();
		themes.addAll( themesManager.bundledThemes );
		themes.sort( (t1, t2) -> t1.name.compareToIgnoreCase( t2.name ) );

		// fill themes list
		themesList.setModel( new AbstractListModel<IJThemeInfo>() {
			@Override
			public int getSize() {
				return themes.size();
			}
			@Override
			public IJThemeInfo getElementAt( int index ) {
				return themes.get( index );
			}
		} );
	}

	private void themesListValueChanged( ListSelectionEvent e ) {
		if( e.getValueIsAdjusting() )
			return;

		EventQueue.invokeLater( () -> {
			setTheme( themesList.getSelectedValue() );
		} );
	}

	private void setTheme( IJThemeInfo themeInfo ) {
		if( themeInfo == null )
			return;

		// change look and feel
		IntelliJTheme.install( getClass().getResourceAsStream( themeInfo.resourceName ) );

		// update all components
		FlatLaf.updateUI();
	}

	@Override
	public void addNotify() {
		super.addNotify();

		selectedCurrentLookAndFeel();
		UIManager.addPropertyChangeListener( lafListener );
	}

	@Override
	public void removeNotify() {
		super.removeNotify();

		UIManager.removePropertyChangeListener( lafListener );
	}

	void lafChanged( PropertyChangeEvent e ) {
		if( "lookAndFeel".equals( e.getPropertyName() ) )
			selectedCurrentLookAndFeel();
	}

	private void selectedCurrentLookAndFeel() {
		LookAndFeel lookAndFeel = UIManager.getLookAndFeel();

		if( !(lookAndFeel instanceof IntelliJTheme.ThemeLaf) )
			themesList.clearSelection();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		JLabel themesLabel = new JLabel();
		themesScrollPane = new JScrollPane();
		themesList = new JList<>();

		//======== this ========
		setLayout(new MigLayout(
			"insets dialog,hidemode 3",
			// columns
			"[grow,fill]",
			// rows
			"[]" +
			"[grow,fill]"));

		//---- themesLabel ----
		themesLabel.setText("Themes:");
		add(themesLabel, "cell 0 0");

		//======== themesScrollPane ========
		{

			//---- themesList ----
			themesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			themesList.addListSelectionListener(e -> themesListValueChanged(e));
			themesScrollPane.setViewportView(themesList);
		}
		add(themesScrollPane, "cell 0 1");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JScrollPane themesScrollPane;
	private JList<IJThemeInfo> themesList;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}