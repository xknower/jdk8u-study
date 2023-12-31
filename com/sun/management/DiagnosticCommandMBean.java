package com.sun.management;

import java.lang.management.PlatformManagedObject;
import javax.management.DynamicMBean;

/**
 * Management interface for the diagnostic commands for the HotSpot Virtual Machine.
 *
 * <p>The {code DiagnosticCommandMBean} is registered to the
 * {@linkplain java.lang.management.ManagementFactory#getPlatformMBeanServer
 * platform MBeanServer} as are other platform MBeans.
 *
 * <p>The {@link javax.management.ObjectName ObjectName} for uniquely identifying
 * the diagnostic MBean within an MBeanServer is:
 * <blockquote>
 *    {@code com.sun.management:type=DiagnosticCommand}
 * </blockquote>
 *
 * <p>This MBean is a {@link javax.management.DynamicMBean DynamicMBean}
 * and also a {@link javax.management.NotificationEmitter}.
 * The {@code DiagnosticCommandMBean} is generated at runtime and is subject to
 * modifications during the lifetime of the Java virtual machine.
 *
 * A <em>diagnostic command</em> is represented as an operation of
 * the {@code DiagnosticCommandMBean} interface. Each diagnostic command has:
 * <ul>
 * <li>the diagnostic command name which is the name being referenced in
 *     the HotSpot Virtual Machine</li>
 * <li>the MBean operation name which is the
 *     {@linkplain javax.management.MBeanOperationInfo#getName() name}
 *     generated for the diagnostic command operation invocation.
 *     The MBean operation name is implementation dependent</li>
 * </ul>
 *
 * The recommended way to transform a diagnostic command name into a MBean
 * operation name is as follows:
 * <ul>
 * <li>All characters from the first one to the first dot are set to be
 *      lower-case characters</li>
 * <li>Every dot or underline character is removed and the following
 *   character is set to be an upper-case character</li>
 * <li>All other characters are copied without modification</li>
 * </ul>
 *
 * <p>The diagnostic command name is always provided with the meta-data on the
 * operation in a field named {@code dcmd.name} (see below).
 *
 * <p>A diagnostic command may or may not support options or arguments.
 * All the operations return {@code String} and either take
 * no parameter for operations that do not support any option or argument,
 * or take a {@code String[]} parameter for operations that support at least
 * one option or argument.
 * Each option or argument must be stored in a single String.
 * Options or arguments split across several String instances are not supported.
 *
 * <p>The distinction between options and arguments: options are identified by
 * the option name while arguments are identified by their position in the
 * command line. Options and arguments are processed in the order of the array
 * passed to the invocation method.
 *
 * <p>Like any operation of a dynamic MBean, each of these operations is
 * described by {@link javax.management.MBeanOperationInfo MBeanOperationInfo}
 * instance. Here's the values returned by this object:
 * <ul>
 *  <li>{@link javax.management.MBeanOperationInfo#getName() getName()}
 *      returns the operation name generated from the diagnostic command name</li>
 *  <li>{@link javax.management.MBeanOperationInfo#getDescription() getDescription()}
 *      returns the diagnostic command description
 *      (the same as the one return in the 'help' command)</li>
 *  <li>{@link javax.management.MBeanOperationInfo#getImpact() getImpact()}
 *      returns <code>ACTION_INFO</code></li>
 *  <li>{@link javax.management.MBeanOperationInfo#getReturnType() getReturnType()}
 *      returns {@code java.lang.String}</li>
 *  <li>{@link javax.management.MBeanOperationInfo#getDescriptor() getDescriptor()}
 *      returns a Descriptor instance (see below)</li>
 * </ul>
 *
 * <p>The {@link javax.management.Descriptor Descriptor}
 * is a collection of fields containing additional
 * meta-data for a JMX element. A field is a name and an associated value.
 * The additional meta-data provided for an operation associated with a
 * diagnostic command are described in the table below:
 * <p>
 *
 * <table border="1" cellpadding="5">
 *   <tr>
 *     <th>Name</th><th>Type</th><th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>dcmd.name</td><td>String</td>
 *     <td>The original diagnostic command name (not the operation name)</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.description</td><td>String</td>
 *     <td>The diagnostic command description</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.help</td><td>String</td>
 *     <td>The full help message for this diagnostic command (same output as
 *          the one produced by the 'help' command)</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.vmImpact</td><td>String</td>
 *     <td>The impact of the diagnostic command,
 *      this value is the same as the one printed in the 'impact'
 *      section of the help message of the diagnostic command, and it
 *      is different from the getImpact() of the MBeanOperationInfo</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.enabled</td><td>boolean</td>
 *     <td>True if the diagnostic command is enabled, false otherwise</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.permissionClass</td><td>String</td>
 *     <td>Some diagnostic command might require a specific permission to be
 *          executed, in addition to the MBeanPermission to invoke their
 *          associated MBean operation. This field returns the fully qualified
 *          name of the permission class or null if no permission is required
 *   </td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.permissionName</td><td>String</td>
 *     <td>The fist argument of the permission required to execute this
 *          diagnostic command or null if no permission is required</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.permissionAction</td><td>String</td>
 *     <td>The second argument of the permission required to execute this
 *          diagnostic command or null if the permission constructor has only
 *          one argument (like the ManagementPermission) or if no permission
 *          is required</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arguments</td><td>Descriptor</td>
 *     <td>A Descriptor instance containing the descriptions of options and
 *          arguments supported by the diagnostic command (see below)</td>
 *   </tr>
 * </table>
 * <p>
 *
 * <p>The description of parameters (options or arguments) of a diagnostic
 * command is provided within a Descriptor instance. In this Descriptor,
 * each field name is a parameter name, and each field value is itself
 * a Descriptor instance. The fields provided in this second Descriptor
 * instance are described in the table below:
 *
 * <table border="1" cellpadding="5">
 *   <tr>
 *     <th>Name</th><th>Type</th><th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arg.name</td><td>String</td>
 *     <td>The name of the parameter</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arg.type</td><td>String</td>
 *     <td>The type of the parameter. The returned String is the name of a type
 *          recognized by the diagnostic command parser. These types are not
 *          Java types and are implementation dependent.
 *          </td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arg.description</td><td>String</td>
 *     <td>The parameter description</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arg.isMandatory</td><td>boolean</td>
 *     <td>True if the parameter is mandatory, false otherwise</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arg.isOption</td><td>boolean</td>
 *     <td>True if the parameter is an option, false if it is an argument</td>
 *   </tr>
 *   <tr>
 *     <td>dcmd.arg.isMultiple</td><td>boolean</td>
 *     <td>True if the parameter can be specified several times, false
 *          otherwise</td>
 *   </tr>
 * </table>
 *
 * <p>When the set of diagnostic commands currently supported by the Java
 * Virtual Machine is modified, the {@code DiagnosticCommandMBean} emits
 * a {@link javax.management.Notification} with a
 * {@linkplain javax.management.Notification#getType() type} of
 * <a href="{@docRoot}/../../../../api/javax/management/MBeanInfo.html#info-changed">
 * {@code "jmx.mbean.info.changed"}</a> and a
 * {@linkplain javax.management.Notification#getUserData() userData} that
 * is the new {@code MBeanInfo}.
 *
 * @since 8
 */
public interface DiagnosticCommandMBean extends DynamicMBean
{

}
