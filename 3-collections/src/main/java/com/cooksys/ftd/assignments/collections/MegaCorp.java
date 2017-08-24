package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import com.cooksys.ftd.assignments.collections.model.WageSlave;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {
	
	HashSet<Capitalist> staffList;
	
	public MegaCorp(){
		staffList = new HashSet<Capitalist>();
	}

    /**
     * Adds a given element to the hierarchy.
     * <p>
     * If the given element is already present in the hierarchy,
     * do not add it and return false
     * <p>
     * If the given element has a parent and the parent is not part of the hierarchy,
     * add the parent and then add the given element
     * <p>
     * If the given element has no parent but is a Parent itself,
     * add it to the hierarchy
     * <p>
     * If the given element has no parent and is not a Parent itself,
     * do not add it and return false
     *
     * @param capitalist the element to add to the hierarchy
     * @return true if the element was added successfully, false otherwise
     */
    @Override
    public boolean add(Capitalist capitalist) {
    	if (capitalist == null){
    		return false;
    	}
    	if (staffList.contains(capitalist)){
    		return false;
    	}
    	if (capitalist.hasParent() && !staffList.contains(capitalist.getParent())){
    		add(capitalist.getParent());
    		return staffList.add(capitalist);
    	}
    	if (!capitalist.hasParent() && capitalist instanceof FatCat){
    		return staffList.add(capitalist);
    	}
        if (!capitalist.hasParent() && !(capitalist instanceof FatCat)) {
        	return false;
        }
    	if (capitalist.hasParent()){
    		return staffList.add(capitalist);
    	} else {
    		if (isParent(capitalist)){
    			return staffList.add(capitalist);
    		} else {
    			return false;
    		}
    	}
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) {
        return staffList.contains(capitalist);
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
        if (staffList.isEmpty()){
        	return new HashSet<Capitalist>();
        }
        return new HashSet<Capitalist>(staffList);
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
    	Capitalist cap;
    	FatCat fc, newFatCat;
    	Set<FatCat> newSet = new HashSet<FatCat>();
        Iterator<Capitalist> it = staffList.iterator();
        while (it.hasNext()){
        	cap = it.next();
        	if (cap instanceof FatCat){
        		fc = (FatCat)cap;
        		newFatCat = new FatCat(fc.getName(), fc.getSalary(), fc.getParent());
        		newSet.add(newFatCat);
        	}
        }
        return newSet;
    }

    /**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {
    	Capitalist cap, newCap, parent;
    	Set<Capitalist> newSet = new HashSet<Capitalist>();
    	
    	if (fatCat == null) {
    		return newSet;
    	}
    	
        Iterator<Capitalist> it = staffList.iterator();
        while (it.hasNext()){
        	cap = it.next();
        	
        	if (cap.hasParent() && cap.getParent().equals(fatCat)){
        		if (cap instanceof FatCat){
        			newCap = new FatCat(cap.getName(), cap.getSalary(), fatCat);
        		} else {
        			newCap = new WageSlave(cap.getName(), cap.getSalary(), fatCat);
        		}
        		newSet.add(newCap);
        	}
//        	if (cap instanceof WageSlave){
//        		newCap = new WageSlave(cap.getName(), cap.getSalary(), fatCat);
//        		newSet.add(newCap);
//        	}
        }
        return newSet;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty. 
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
    	Map<FatCat, Set<Capitalist>> newMap = new HashMap<FatCat, Set<Capitalist>>();
    	FatCat parent, newParent;
    	Set<FatCat> parents = getParents();
    	Iterator<FatCat> it = parents.iterator();
    	while (it.hasNext()){
    		parent = it.next();
    		newParent = new FatCat(parent.getName(), parent.getSalary(), parent.getParent());
    		newMap.put(newParent, getChildren(parent));
    	}
    	return newMap;
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {
        ArrayList<FatCat> fcArr = new ArrayList<FatCat>();
        
        if (capitalist == null){
        	return fcArr;
        }
        FatCat parent;
        while (capitalist != null && capitalist.hasParent()){
        	parent = capitalist.getParent();
        	if (has(parent)){
        		fcArr.add( new FatCat(parent.getName(), parent.getSalary(), parent.getParent()) );
        	}
        	capitalist = parent;
        }
        return fcArr;
    }
    
    
    /**
     * @param capitalist
     * @return true if the given element is of type FatCat
     * and has at least one child element of any type
     */
    private boolean isParent(Capitalist capitalist){
    	if ( !(capitalist instanceof FatCat) ){
    		return false;
    	}
    	FatCat fc = (FatCat)capitalist;
    	if (getChildren(fc).size() == 0){
    		return false;
    	}
    	return true;
    }
}
