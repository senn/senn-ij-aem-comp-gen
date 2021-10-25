# senn-ij-aem-comp-gen // AEM Component Generator - IntelliJ Plugin

An IntelliJ plugin to easily create AEM components. <br />
It has 
<ul>
<li>an easy to use UI</li>
<li>creation of all/most files associated with components (content xml, html, dialog xml, edit config xml, client libs and sling model)</li>
<li>ad hoc parameters for creation (that are stored in memory in the IntelliJ session for ease of use)</li>
<li>the option to automatically open the created files in the editor</li>
</ul>

The plugin was created as a tool to be used by myself and optionally also other AEM developers. 
And also as a way to re-familiarize myself with Git. 
<br /><br />
It was in <i>no way</i> intended to follow best practices in IntelliJ plugin development guidelines.  
It doesn't use the new Kotlin/Gradle approach and it does all operations directly to the filesystem using java.io 
instead of using IntelliJ's built-in Virtual Filesystem, PSI system and templating.
<br />
To use the plugin, download the latest release and drop it inside the <code>plugins</code> folder of your IntelliJ.  
Ideally using this hierarchy: <code>plugins/senn-ij-aem-comp-gen/lib/<b>senn-ij-aem-comp-gen.jar</b></code> but that's not required.
Then start IntelliJ and rightclick on a folder or file and you should see the <code>New > AEM Component Files</code> option.
<br /><br />
If you do use this plugin, feel free to let me know!<br/>
Cheers, <br />
Bart
