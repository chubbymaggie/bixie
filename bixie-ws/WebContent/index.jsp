<?xml version="1.0" encoding="US-ASCII" ?>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.joogie.org/jsp/jstl/functions" prefix="funcs"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head profile="http://www.w3.org/2005/10/profile">
<link rel="icon" 
      type="image/ico" 
      href="img/favicon.ico">

<meta charset='utf-8'>
<meta http-equiv="X-UA-Compatible" content="chrome=1">
	<meta name="description"
		content="Bixie : Find contradictions in Java code">
		<title>Bixie : Find contradictions in Java code</title>
		<link rel="stylesheet" type="text/css" media="screen" href="css/stylesheet.css"/>

</head>
<body>

	<!-- HEADER -->
	<div id="header_wrap" class="outer">
		<header class="inner"> <a id="forkme_banner"
				href="https://github.com/martinschaef/bixie">View on GitHub</a>
	
			<h1 id="project_title">Find contradictions in Java code</h1>
	
			<section id="downloads"> <a class="zip_download_link"
				href="https://github.com/martinschaef/bixie/releases/download/v1.0/bixie.jar.zip">Download
				the .zip file</a> 
			</section> 
		</header>
	</div>

	<!-- MAIN CONTENT -->
	<div id="main_content_wrap" class="outer">
		<section id="main_content" class="inner">

	<p>
		Bixie is a static checker that detects <b>contradictions</b> in Java code. A contradiction
		is piece of code that only occurs on paths that contain conflicting assumptions. That is, it is either unreachable
		or any of its executions must lead to an error.
		While contradictions are not automatically bugs, they have a bad smell as they represent code that 
		cannot be executed safely. The Java compiler, for example, treats certain instances of contradictions 
		as errors, like the use of uninitialized variables or inevitable null-pointer dereferences. 
		Bixie uses deductive verification to find those contradictions that the Java compiler missed. 
	</p>

	<div >
		<div id="prompt">
		<a href="./bixie" target="_blank">
		<img id="screenshot" alt="Write some Java code here!"
						src="img/screen.png" />
		</a>
		</div>
		<br/>
		<p> 
			The easiest way to understand what Bixie does is to
			try it <a href="./bixie" target="_blank">online</a>. Click on the picture
			on the right and browse through some examples of contradictions
			in Java code, or type your own program an check it.
		</p>
		
	</div>
	<h3>Demo Video</h3>
	<div id="thevideo" class="center">
	<iframe width="600" height="315" src="//www.youtube.com/embed/dQw4w9WgXcQ?rel=0" frameborder="0" align="middle"></iframe>	
	<br/>
	<hr/>
	</div>
	
	<h3>Contradictions Found by Bixie</h3>
		<p>
		We keep running Bixie on open-source projects and report our findings. 
		In order to avoid spamming developers, we inspect each warning manually to make sure that it is relevant.
		We only create pull requests if the warning does not involve code that is generated, 
		deliberately unreachable (e.g., a debug constant disables it), or the code has a comment that 
		says it is supposed to be unreachable. 
		<br/>
		Here are some of the more contradictions found and fixed by Bixie in popular projects: 
		</p>
		<ul>
		<li>Apache Cassandra: <a href="https://github.com/apache/cassandra/pull/46" target="_blank">see pull request</a></li>
		<li>Apache jMeter: <a href="https://github.com/apache/jmeter/pull/10" target="_blank">see pull request</a></li> 
		<li>Apache Tomcat: <a href="https://github.com/apache/tomcat/pull/13" target="_blank">see pull request</a></li>
		<li>Bouncy Castle: <a href="https://github.com/bcgit/bc-java/pull/87" target="_blank">see pull request</a></li>
		<li> Soot: see pull request <a href="https://github.com/Sable/soot/pull/244" target="_blank"> 1</a> and 
		<a href="https://github.com/Sable/soot/pull/261" target="_blank">2</a> and 
		<a href="https://github.com/Sable/soot/pull/260" target="_blank">3</a> </li>
		</ul>
	During the development of Bixie we also found a case where the Java compiler generates
	unreachable bytecode (see <a href="http://stackoverflow.com/questions/25615417/try-with-resources-introduce-unreachable-bytecode" target="_blank">here</a>).

	<h3>Download and Usage</h3>
	<p>Before you start, check your Java version. 
	You need at least  <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html" target="_blank">JDK 7</a> 
	to run Bixie. Please check:
	</p>
	<pre>java -version</pre>
	<p>If the result is not <code>1.7.0</code> or higher, please update your Java version, or, 
	if you just want to play with Bixie, use our <a href="./bixie" target="_blank">web tester</a>.
	</p> 
	
	<p>For a quick start, download the all-in-one 
	<a href="https://github.com/martinschaef/bixie/releases/download/v1.0/bixie.jar.zip">jar file
	</a>. Now you can run Bixie as follows: 
	</p>
    <pre>java -jar bixie.jar -j [input] -cp [classpath] -o [output] </pre>
    <p>
	Where <code>[input]</code> is either a (debug compiled) Jar file, or the root folder of your class or source files.
	Note that <a href="https://github.com/Sable/soot" target="_blank">Soot</a>, which we use for parsing, runs much 
	better on class files. 
	</p>
    <p>
	For <code>[classpath]</code> use the classpath that you would also use to run the code 
	that you passed as <code>[input]</code>. If no special classpath is required, use the same
	value as <code>[input]</code>. 
	</p>

    <p>
	For <code>[out]</code> use the name of the text file where Bixie should write its report to. 
	</p>
	
	<p>
	For larger programs, we highly recommend to start Bixie with a lot of resources. In our experiments, we use the
	following setup (which requires a 64bit JDK):
	</p>
	<pre>java -Xmx2g -Xms2g -Xss4m -jar bixie.jar ... </pre>
	<p>For 32bit installations of Java use -Xmx1g -Xms1g -Xss4m.</p> 
	
	<h5>Example</h5>
	<p>
	To check if everything is working properly, download the <a href="https://github.com/martinschaef/bixie/releases/download/v1.0/bixie.jar.zip">jar file</a>
	and <a href="demo/Demo.java">Demo.java</a> and put them in the same folder. Now go to that folder and run:</p>
    <pre>java -jar bixie.jar -j ./ -cp ./ -o report.txt </pre>	 
	<p>Your result.txt file should look somewhat like this:</p>
	<pre>In file: ./Demo.java
		line 39
		line 65</pre>
	
	<h3>Previous Tools and Papers</h3>
	<p>
	Bixie is the successor of our  <a href="http://www.joogie.org" target="_blank">Joogie</a> tool. 
	While Joogie was already doing a good job in detecting contradictions in Java bytecode, it produced 
	a substantial amount of false alarms because not all contradictions in the bytecode are
	also contradictions in Java code. In Bixie, we use a novel technique to translate bytecode into logic
	that allows our checker to suppress almost all false alarms. At the same time, we improved
	handling of loops and library functions to further increase the detection rate.
	For more details on the new features of Bixie, check our 
	<a href="https://github.com/martinschaef/jar2bpl/wiki" target="_blank">wiki</a>.
	</p>
	<p>
	The papers below describe who the actual checking for contradictions is implemented in Bixie:</p>
	<ul> 
	<li><a href="http://iist.unu.edu/publication/theory-control-flow-graph-exploration" target="_blank">A Theory for Control-Flow Graph Exploration</a>,  S. Arlt, P. R&uuml;mmer, M. Sch&auml;f, ATVA 2013 </li>
	<li><a href="http://www.informatik.uni-freiburg.de/~schaef/joogie.pdf" target="_blank">Joogie: Infeasible Code Detection for Java</a>,  S. Arlt, M. Sch&auml;f, CAV 2012 </li>
	<li><a href="http://cs.nyu.edu/~wies/publ/doomed_program_points.pdf" target="_blank">It's doomed; we can prove it</a>,  J. Hoenicke, R. Leino, A. Podelski, M. Sch&auml;f, T. Wies, FM 2009 </li>
	</ul>
	</section>
	
</div>

	<div id="footer_wrap" class="footer">
		<p >
			Bixie is maintained by <a href="https://github.com/martinschaef" target="_blank">martinschaef</a>
		</p>
	</div>

	<script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
          </script>
	<script type="text/javascript">
            try {
              var pageTracker = _gat._getTracker("UA-20025374-5");
            pageTracker._trackPageview();
            } catch(err) {}
    </script>


</body>
</html>