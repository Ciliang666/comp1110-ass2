Reviewer: Junming Zhao(u6803148)
Component: <Viewer.RemoveSubstring(String tilePiece)> and part of <Viewer.makeControls(String tilePiece)>
Author: Jingsheng Deng (u6847863)

Review Comments:

1.Code features:
Code has good method names (e.g.remove substring) and clear local variables, which gives the code 
a nice readability in general. Good use of lambda functions within event handling. Code features 
in nice use of looping with clear indices as well. Helper function is created properly when needed.

2.Documents:
Nice docstring about methods in general, however lack of comments within a method.

3.Structure:
Good coding structure is presented (e.g. using helper function).

4.Conventions following:
The names of methods and variables are pretty good in general. However method names shouldn't be 
capitalised by convention. (Refers to 'RemoveSubstring') Also spaces should be inserted within
assignment (e.g. 'i+=5' could be 'i += 5' by convention to enhance readability). 
Thus the programming style in terms of convention following could be done in a better way.

5.Potential error:
In the method of RemoveSubstring(String in, String remove), lengths of input and remove string 
are not considered thoroughly. If the length of 'in' and 'remove' is smaller than 2, it would
lead to an error (StringIndexOutOfBoundsException). Although technically this method will be
safely used in this specific class, code dealing with unexpected length of string can be added
to prevent any potential error (i.e. use try/throws or just return the input string straight away). 
 

