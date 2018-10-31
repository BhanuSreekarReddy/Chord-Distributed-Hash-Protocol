# Chord-Distributed-Hash-Protocol
This project is about implementing Chord Protocol

The Chord Protocol has been implemented in single java file ChordProtocol.java

The compiling and execution has been automated with a shell script "makefile.sh"

Compiling and executing the program

1) If the input commands is through command line

> ./makefile.sh chord < n >

2) If the input commands are given from file

> ./makefile.sh chord -i < file name > < n >

Commands to work on chord protocol

The program will accept the following commands either interactively or in batch mode. Emit errors if the commands are ill formed in any way. Common errors are node does not exist; incorrect number of parameters; etc.

end -- stop the program without saying anything.
add <id> -- add node to ring with given id.
drop <id> -- remove node with given id from ring.
join <from> <to> -- join node from with the node to. Join should be call only once right after a node is added.
fix <id> -- fix the finger table for given node.
stab <id> -- invoke stabilize method on given node.
list -- show the id for each node in the ring.
show <id> -- show the successor, predecessor, and finger table for the given node.
Output
Please follow the required output carefully to ease grading of your assignment.

General
ERROR: invalid integer <n>
ERROR: node id must be in [0,<n>)
SYNTAX ERROR: <cmd> expects <n> parameters not <m>
ERROR: Node <n> does not exist
ERROR: Node <n> exists
No output--that means no output--except for errors.
end
join
fix
stab
add
Added node <n>
drop
Dropped node <n>
list (order least to greatest)
Nodes: <n1>, <n2>, ..., <nM>
show (for each node, order least to greatest)
Node <n>: suc <n1>, pre <n2 or None>: finger <f1>,<f2>,...,<fM>
