===========================
External Control of Gitools
===========================

As of version 1.7.0 Gitools can be addressed from outside of the application. At the moment there is one command availabe.

Gitools is listening to the port **50151** by default. So it is possible to send the commands to this port. Alternatively it is possible to change the port by editing the **ui.xml** file in the Gitools configuration folder. (For Linux and Mac OS X users this should be at **/.gitools**).


Overview of commands
-------------------------------------------------

================================================  ============================================  ========================================================================================= ======================
 Command                                           Description                                   Options                                                                                  as of Version
================================================  ============================================  ========================================================================================= ======================
**add-header-colored-labels**                      Adds a new colored labels header                                                                                                        2.0.0
                                                                                                 - ``-c (--color) <color>``     : A Hex color as '#FF0000' - depends on --value
                                                                                                 - ``-n (--no-auto-generate)``  : Specify if auto color generation is not desired.
                                                                                                 - ``-p (--pattern) <pattern>`` : The pattern of annotations as e.g. ${annotation-id}
                                                                                                 - ``-d (--dimension) <dim>``   : Indicate where to add the header: 'rows' or 'columns'.
                                                                                                 - ``-t (--text-visible)``      : Set for visible text labels
                                                                                                 - ``-v (--value) <value>``     : A value corresponding to a color
                                                                                                 - ``-h (--heatmap) <heatmap>`` : (Optional) Heatmap name or ``last`` for the last added

**add-header-text-labels**                         Adds a new text header to the heatmap
                                                                                                 - ``-p (--pattern) <pattern>`` : The pattern of annotations as e.g. ${annotation-id}      2.0.0
                                                                                                 - ``-d (--dimension) <dim>``   : Indicate where to add the header: 'rows' or 'columns'.
                                                                                                 - ``-h (--heatmap) <heatmap>`` : Heatmap name. Not compulsory if one is open.
**close**                                          Closes a heatmap and optionally saves (as)
                                                                                                 - ``-a (--as) <FILE_NAME>``    : path and filename indicating where to save the heatmap   2.2.1
                                                                                                 - ``-d (--discard-hidden)``    : Discard hidden data
                                                                                                 - ``-h (--heatmap) <heatmap>`` : (optional) 'LAST' or heatmap name
                                                                                                 - ``-o (--optimize)``          : Optimize data file (slower saving process)
                                                                                                 - ``-s (--save)``              : Save current state as heatmap
**help**                                           Prints all available commands
**load **\ matrix-file                             Tells Gitools to load a file.                 - ``--rows** (-r)``     : File rows annotations                                           1.7.0
                                                                                                 - ``--cols (-c)``     : File cols annotations
**sort-by-annotation**                             Sorts the heatmap by col or row annotation
                                                                                                 - ``-p (--pattern) <pattern>`` : The pattern of annotations as e.g. ${annotation-id}      2.2.0
                                                                                                 - ``-d (--dimension) <dim>``   : Indicate where to add the header: 'rows' or 'columns'.
                                                                                                 - ``-h (--heatmap) <heatmap>`` : Heatmap name. Not compulsory if one is open.
**sort-mutex**                                     Sorts the visible layer by mutual excl.
                                                                                                 - ``-d (--dimension) <dimension>``   : Indicate where to add the header: 'rows' or        2.2.1
                                                                                                   'columns'.
                                                                                                 - ``-h (--heatmap) <heatmap>``       : (optional) 'LAST' or heatmap name
                                                                                                 - ``-i (--identifier) <identifier>`` : Specify this option for each identifier which
                                                                                                   you want to include in the mutex sorting
                                                                                                 - ``-p (--pattern) <pattern>``       : The pattern of annotations as e.g.
                                                                                                   ${annotation-id}
                                                                                                 - ``-s (--sort) <sort>``             : Sort according to header. Specify either
                                                                                                   asc[ending] or desc[ending].

**version**                                        Prints Gitools version.                                                                                                                 1.7.1
================================================  ============================================  ========================================================================================= ======================



How to send a command to Gitols
-------------------------------------------------

There is several ways to send the commands listed above to Gitools. We will make three examples here:

Command line
.................................................

With a terminal application that lets you execute command line you can create a new Gitools instance with command to execute upon startup.


.. code-block:: bash

    $ gitools load /home/user/matrix-file.tdm --cols /home/user/col-annotations.tsv --rows /home/user/row-annotations.tsv


HTTP
.................................................

By HTTP it is possible to send the command like this:

.. code-block:: bash

    $ http://localhost:50151/load?file=/home/user/matrix-file.tdm&cols=/home/user/col-annotations.tsv&rows=/home/user/row-annotations.tsv 


Python
.................................................

Python or any other programming environment can make use of the  `Telnet <http://en.wikipedia.org/wiki/Telnet>`__ 
internet protocol. See below to understand how python can connect to Gitools and send it a command.

.. code-block:: python

    import socket
    def gitools_command(command):
        gitools = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        gitools.connect(("localhost", 50151))
        gitools.sendall( command.encode('utf-8'))
        gitools.close()

    command = "load /home/user/matrix-file.tdm --cols /home/user/col-annotations.tsv --rows /home/user/row-annotations.tsv"
    gitools_command(command)