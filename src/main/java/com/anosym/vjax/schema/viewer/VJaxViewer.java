/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VJaxViewer.java
 *
 * Created on Nov 20, 2011, 11:30:00 AM
 */
package com.anosym.vjax.schema.viewer;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.anosym.vjax.VMarshallerConstants;
import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author marembo
 */
public final class VJaxViewer extends javax.swing.JFrame {

    private class VTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getNewLeadSelectionPath();
            if (path != null) {
                node = (DefaultMutableTreeNode) path.getLastPathComponent();
                setSelectedElement((VElement) node.getUserObject());
            }
        }
    }

    /** Creates new form VJaxViewer */
    public VJaxViewer() {
        initComponents();
        this.jTree1.setEditable(true);
        this.jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jTree1.getSelectionModel().addTreeSelectionListener(new VTreeSelectionListener());
        setSelectedElement(new VElement("elem"));
    }

    public void setDocument(VDocument doc) {
        //create the model
        VElement elem = doc.getRootElement();
        node = new DefaultMutableTreeNode(elem);
        TreeModel model = new DefaultTreeModel(node);
        addNode(elem, node);
        this.setDataModel(model);
    }

    private void addNode(VElement parent, DefaultMutableTreeNode node) {
        for (VElement el : parent.getChildren()) {
            DefaultMutableTreeNode anode = new DefaultMutableTreeNode(el);
            node.add(anode);
            if (el.hasChildren()) {
                addNode(el, anode);
            }
        }
    }
    private VjaxViewerListener viewerListener;

    public void setViewerListener(VjaxViewerListener viewerListener) {
        this.viewerListener = viewerListener;
    }
    private TreeModel dataModel;
    public static final String PROP_DATAMODEL = "dataModel";
    private VElement selectedElement;
    public static final String PROP_SELECTEDELEMENT = "selectedElement";
    private DefaultMutableTreeNode node = null;

    /**
     * Get the value of selectedElement
     *
     * @return the value of selectedElement
     */
    public VElement getSelectedElement() {
        return selectedElement;
    }

    /**
     * Set the value of selectedElement
     *
     * @param selectedElement new value of selectedElement
     */
    public void setSelectedElement(VElement selectedElement) {
        VElement oldSelectedElement = this.selectedElement;
        this.selectedElement = selectedElement;
        propertyChangeSupport.firePropertyChange(PROP_SELECTEDELEMENT, oldSelectedElement, selectedElement);
        if (this.selectedElement != null) {
            this.propertyValuejTextField.setEditable(!this.selectedElement.isParent());
            if (this.selectedElement.getMarkup().equals(VMarshallerConstants.COLLECTION_ELEMENT_MARKUP)) {
                this.newjButton.setEnabled(true);
            } else {
                this.newjButton.setEnabled(false);
            }
        }
    }

    /**
     * Get the value of dataModel
     *
     * @return the value of dataModel
     */
    public TreeModel getDataModel() {
        return dataModel;
    }

    /**
     * Set the value of dataModel
     *
     * @param dataModel new value of dataModel
     */
    public void setDataModel(TreeModel dataModel) {
        TreeModel oldDataModel = this.dataModel;
        this.dataModel = dataModel;
        propertyChangeSupport.firePropertyChange(PROP_DATAMODEL, oldDataModel, dataModel);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTree1 = new javax.swing.JTree();
    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();
    jTextField1 = new javax.swing.JTextField();
    propertyValuejTextField = new javax.swing.JTextField();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jPanel3 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    newjButton = new javax.swing.JButton();
    savejButton = new javax.swing.JButton();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenuItem1 = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

    jSplitPane1.setDividerLocation(400);

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${dataModel}"), jTree1, org.jdesktop.beansbinding.BeanProperty.create("model"));
    bindingGroup.addBinding(binding);

    jTree1.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
      public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
        jTree1TreeExpanded(evt);
      }
      public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
        jTree1TreeCollapsed(evt);
      }
    });
    jScrollPane1.setViewportView(jTree1);

    jSplitPane1.setLeftComponent(jScrollPane1);

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    jPanel2.setLayout(new java.awt.GridLayout(0, 2));

    jTextField1.setEditable(false);

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.toString}"), jTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    jPanel2.add(jTextField1);

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.content}"), propertyValuejTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    jPanel2.add(propertyValuejTextField);

    org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${selectedElement.attributes}");
    org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, jTable1);
    org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
    columnBinding.setColumnName("Name");
    columnBinding.setColumnClass(String.class);
    columnBinding.setEditable(false);
    columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${value}"));
    columnBinding.setColumnName("Value");
    columnBinding.setColumnClass(String.class);
    bindingGroup.addBinding(jTableBinding);
    jTableBinding.bind();
    jScrollPane2.setViewportView(jTable1);

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Description:"));
    jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.comment}"), jLabel3, org.jdesktop.beansbinding.BeanProperty.create("text"));
    bindingGroup.addBinding(binding);

    jPanel3.add(jLabel3);

    newjButton.setText("New");
    newjButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newjButtonActionPerformed(evt);
      }
    });

    savejButton.setText("Save");
    savejButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        savejButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
          .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
          .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 791, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(savejButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(newjButton)))
        .addContainerGap())
    );

    jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {newjButton, savejButton});

    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(27, 27, 27)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(newjButton)
          .addComponent(savejButton))
        .addContainerGap(34, Short.MAX_VALUE))
    );

    jSplitPane1.setRightComponent(jPanel1);

    getContentPane().add(jSplitPane1);

    jMenu1.setText("File");

    jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem1.setText("Open......");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem1ActionPerformed(evt);
      }
    });
    jMenu1.add(jMenuItem1);

    jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem2.setText("Save......");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem2ActionPerformed(evt);
      }
    });
    jMenu1.add(jMenuItem2);

    jMenuBar1.add(jMenu1);

    setJMenuBar(jMenuBar1);

    bindingGroup.bind();

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if (viewerListener != null) {
            viewerListener.doOpen();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        if (viewerListener != null) {
            viewerListener.doSave();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTree1TreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree1TreeExpanded
        // TODO add your handling code here:
//        TreePath path = evt.getPath();
//        node = (DefaultMutableTreeNode) path.getLastPathComponent();
//        if (node != null) {
//            setSelectedElement((VElement) node.getUserObject());
//        }
    }//GEN-LAST:event_jTree1TreeExpanded

    private void jTree1TreeCollapsed(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree1TreeCollapsed
        // TODO add your handling code here:
//        TreePath path = evt.getPath();
//        node = (DefaultMutableTreeNode) path.getLastPathComponent();
//        if (node != null) {
//            setSelectedElement((VElement) node.getUserObject());
//        }
    }//GEN-LAST:event_jTree1TreeCollapsed

    private void savejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savejButtonActionPerformed
        // TODO add your handling code here:
        if (this.viewerListener != null) {
            this.viewerListener.doSave();
        }
    }//GEN-LAST:event_savejButtonActionPerformed

    private void newjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newjButtonActionPerformed
        // TODO add your handling code here:
        //we only add if the element has children
        if (node != null && selectedElement != null && selectedElement.hasChildren() && selectedElement.getMarkup().equals(VMarshallerConstants.COLLECTION_ELEMENT_MARKUP)) {
            try {
                //be sure that it is only map or collections
                VElement child = selectedElement.getChildren().get(0);
                VElement newChild = child.clone();
                VAttribute attr = newChild.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
                if (attr != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(1970, 0, 1);
                    attr.setValue((cal.getTimeInMillis()) + "");
                }
                selectedElement.addChild(newChild);
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newChild);
                newChild.setContent("");
                ((DefaultTreeModel) getDataModel()).insertNodeInto(newNode, node, node.getChildCount());
                if (newChild.hasChildren()) {
                    addNewNode(newChild, newNode);
                }
                jTree1.scrollPathToVisible(new TreePath(newNode.getPath()));
            } catch (Exception ex) {
                Logger.getLogger(VJaxViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_newjButtonActionPerformed
    private void addNewNode(VElement parent, DefaultMutableTreeNode parentNode) {
        for (VElement e : parent.getChildren()) {
            e.setContent("");
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(e);
            ((DefaultTreeModel) getDataModel()).insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            VAttribute attr = e.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
            if (attr != null) {
                Calendar cal = Calendar.getInstance();
                cal.set(1970, 0, 1);
                attr.setValue((cal.getTimeInMillis()) + "");
            }
            if (e.hasChildren()) {
                addNewNode(e, childNode);
            }
        }
    }

    public static void view(VDocument document) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(VJaxViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        VJaxViewer viewer = new VJaxViewer();
        viewer.setTitle(document.getRootElement().toString());
        viewer.setDocument(document);
        viewer.setExtendedState(VJaxViewer.MAXIMIZED_BOTH);
        viewer.setVisible(true);
    }

    public static void view(VDocument document, VjaxViewerListener listener) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(VJaxViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        VJaxViewer viewer = new VJaxViewer();
        viewer.setViewerListener(listener);
        viewer.setTitle(document.getRootElement().toString());
        viewer.setDocument(document);
        viewer.setExtendedState(VJaxViewer.MAXIMIZED_BOTH);
        viewer.setVisible(true);
    }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel3;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jMenuItem1;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JTextField jTextField1;
  private javax.swing.JTree jTree1;
  private javax.swing.JButton newjButton;
  private javax.swing.JTextField propertyValuejTextField;
  private javax.swing.JButton savejButton;
  private org.jdesktop.beansbinding.BindingGroup bindingGroup;
  // End of variables declaration//GEN-END:variables
}
