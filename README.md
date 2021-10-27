# senn-ij-aem-comp-gen // AEM Component Generator - IntelliJ Plugin

<h2>About</h2>
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

<h2>How to use</h2>

To use the plugin, download the latest release and drop it inside the <code>plugins</code> folder of your IntelliJ.  
Ideally using this hierarchy: <code>plugins/senn-ij-aem-comp-gen/lib/<b>senn-ij-aem-comp-gen.jar</b></code> but that's not required.
Then start IntelliJ and rightclick on a folder or file and you should see the <code>New > AEM Component Files</code> option.
<br /><br />

Click the context menu from anywhere within your project navigation: <code>New > AEM Component Files</code>
<br />
![context menu](https://i.imgur.com/MAkL74M.png)
<br />
A popup dialog appears providing options what to create and where to place the generated files.
Fill in the required info, select which files to generate and indicate whether to open the files in the editor after creation.
<br />
As an example, we'll create a Crypto component.  We want to generate all the files and open them in the editor:
![dialog](https://i.imgur.com/lOWjOJH.png)
<br />
Click OK and ...<br />
![created files](https://i.imgur.com/gMq80a4.png) ![created sling model](https://i.imgur.com/kgQzKDk.png)
<br />
The files will be opened in the editor if the option was selected.
<br /><br />
When creating another AEM Component, the plugin remembers the options you entered and selected the previous time and you only <i>need</i>
to fill in the component name and component group.

<span style="color: red;">It's important to remark that the plugin only works if the AEM parent module (ie. aem-guides-wknd) is 
the <u>project root</u> in IntelliJ containing the modules for ui.apps, core, etc.<br />
If you have another project root and the AEM parent module is a subfolder within that project, the files will not be placed in the
correct location.<br />
It should work for normal AEM project setups.</span>


Cheers, <br />
Bart
