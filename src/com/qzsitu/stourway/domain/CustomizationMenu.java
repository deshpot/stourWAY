package com.qzsitu.stourway.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "STSD_Customization_Menu")
public class CustomizationMenu {
	String id;
	String menuName;
	String menuDescription;
	List<CustomizationMenu>	childMenuList;
	CustomizationXMod xmod;
}
