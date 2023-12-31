package com.sun.jndi.toolkit.dir;

import javax.naming.*;
import javax.naming.directory.SearchControls;
import java.util.*;

/**
  * A class for recursively enumerating the contents of a Context;
  *
  * @author Jon Ruiz
  */
public class ContextEnumerator implements NamingEnumeration<Binding> {

    private static boolean debug = false;
    private NamingEnumeration<Binding> children = null;
    private Binding currentChild = null;
    private boolean currentReturned = false;
    private Context root;
    private ContextEnumerator currentChildEnum = null;
    private boolean currentChildExpanded = false;
    private boolean rootProcessed = false;
    private int scope = SearchControls.SUBTREE_SCOPE;
    private String contextName = "";

    public ContextEnumerator(Context context) throws NamingException {
        this(context, SearchControls.SUBTREE_SCOPE);
    }

    public ContextEnumerator(Context context, int scope)
        throws NamingException {
            // return this object except when searching single-level
        this(context, scope, "", scope != SearchControls.ONELEVEL_SCOPE);
   }

    protected ContextEnumerator(Context context, int scope, String contextName,
                             boolean returnSelf)
        throws NamingException {
        if(context == null) {
            throw new IllegalArgumentException("null context passed");
        }

        root = context;

        // No need to list children if we're only searching object
        if (scope != SearchControls.OBJECT_SCOPE) {
            children = getImmediateChildren(context);
        }
        this.scope = scope;
        this.contextName = contextName;
        // pretend root is processed, if we're not supposed to return ourself
        rootProcessed = !returnSelf;
        prepNextChild();
    }

    // Subclass should override if it wants to avoid calling obj factory
    protected NamingEnumeration<Binding> getImmediateChildren(Context ctx)
        throws NamingException {
            return ctx.listBindings("");
    }

    // Subclass should override so that instance is of same type as subclass
    protected ContextEnumerator newEnumerator(Context ctx, int scope,
        String contextName, boolean returnSelf) throws NamingException {
            return new ContextEnumerator(ctx, scope, contextName, returnSelf);
    }

    public boolean hasMore() throws NamingException {
        return !rootProcessed ||
            (scope != SearchControls.OBJECT_SCOPE && hasMoreDescendants());
    }

    public boolean hasMoreElements() {
        try {
            return hasMore();
        } catch (NamingException e) {
            return false;
        }
    }

    public Binding nextElement() {
        try {
            return next();
        } catch (NamingException e) {
            throw new NoSuchElementException(e.toString());
        }
    }

    public Binding next() throws NamingException {
        if (!rootProcessed) {
            rootProcessed = true;
            return new Binding("", root.getClass().getName(),
                               root, true);
        }

        if (scope != SearchControls.OBJECT_SCOPE && hasMoreDescendants()) {
            return getNextDescendant();
        }

        throw new NoSuchElementException();
    }

    public void close() throws NamingException {
        root = null;
    }

    private boolean hasMoreChildren() throws NamingException {
        return children != null && children.hasMore();
    }

    private Binding getNextChild() throws NamingException {
        Binding oldBinding = children.next();
        Binding newBinding = null;

        // if the name is relative, we need to add it to the name of this
        // context to keep it relative w.r.t. the root context we are
        // enumerating
        if(oldBinding.isRelative() && !contextName.equals("")) {
            NameParser parser = root.getNameParser("");
            Name newName = parser.parse(contextName);
            newName.add(oldBinding.getName());
            if(debug) {
                System.out.println("ContextEnumerator: adding " + newName);
            }
            newBinding = new Binding(newName.toString(),
                                     oldBinding.getClassName(),
                                     oldBinding.getObject(),
                                     oldBinding.isRelative());
        } else {
            if(debug) {
                System.out.println("ContextEnumerator: using old binding");
            }
            newBinding = oldBinding;
        }

        return newBinding;
    }

    private boolean hasMoreDescendants() throws NamingException {
        // if the current child is expanded, see if it has more elements
        if (!currentReturned) {
            if(debug) {System.out.println("hasMoreDescendants returning " +
                                          (currentChild != null) ); }
            return currentChild != null;
        } else if (currentChildExpanded && currentChildEnum.hasMore()) {

            if(debug) {System.out.println("hasMoreDescendants returning " +
                "true");}

            return true;
        } else {
            if(debug) {System.out.println("hasMoreDescendants returning " +
                "hasMoreChildren");}
            return hasMoreChildren();
        }
    }

    private Binding getNextDescendant() throws NamingException {

        if (!currentReturned) {
            // returning parent
            if(debug) {System.out.println("getNextDescedant: simple case");}

            currentReturned = true;
            return currentChild;

        } else if (currentChildExpanded && currentChildEnum.hasMore()) {

            if(debug) {System.out.println("getNextDescedant: expanded case");}

            // if the current child is expanded, use it's enumerator
            return currentChildEnum.next();

        } else {

            // Ready to go onto next child
            if(debug) {System.out.println("getNextDescedant: next case");}

            prepNextChild();
            return getNextDescendant();
        }
    }

    private void prepNextChild() throws NamingException {
        if(hasMoreChildren()) {
            try {
                currentChild = getNextChild();
                currentReturned = false;
            } catch (NamingException e){
                if (debug) System.out.println(e);
                if (debug) e.printStackTrace();
            }
        } else {
            currentChild = null;
            return;
        }

        if(scope == SearchControls.SUBTREE_SCOPE &&
           currentChild.getObject() instanceof Context) {
            currentChildEnum = newEnumerator(
                                          (Context)(currentChild.getObject()),
                                          scope, currentChild.getName(),
                                          false);
            currentChildExpanded = true;
            if(debug) {System.out.println("prepNextChild: expanded");}
        } else {
            currentChildExpanded = false;
            currentChildEnum = null;
            if(debug) {System.out.println("prepNextChild: normal");}
        }
    }
}
