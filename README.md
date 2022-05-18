# ADBS-CW

## This is a toy in-memory database system I implement in the course ADBS, which includes query minimization and query evaluation




The logic of capturing join condition is that, firstly join the 
left relation and right relation to get the joined relation by using the joinRelation() method 
 in the JoinOperator class during query planning, then let the joined relation and the list of all comparison atoms
be the arguments of check() method in SelectCheck() class to check whether the joined relatioin is satisfied with
the condition (the check() method can determine which comparison atoms is useful to the joined atom.) The joinRelation()
 method also can avoid the duplicate
terms appear in both of the left and right child atoms, which described in the comment of joinRelation() method.

