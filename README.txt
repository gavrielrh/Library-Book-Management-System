  _      ____  __  __  _____
 | |    |  _ \|  \/  |/ ____|
 | |    | |_) | \  / | (___
 | |    |  _ <| |\/| |\___ \
 | |____| |_) | |  | |____) |
 |______|____/|_|  |_|_____/

---------------------------------------------------------

[Description]
The Library Book Management System(LBMS) is the system used by the Book Worm Library(BWL)
to provide book information to users, track library visitor statistics for a library statistics report,
track borrowed out books, and make updates to the library’s inventory.
The system will provide an API used by a client-side interface that will be used by a BWL employee.

---------------------------------------------------------

[Setup]
LBMS requires a text file named "books.txt" with a listing of all the possible books for purchasing.

---------------------------------------------------------

[Authors]
Junwen Mai <jxm7861@rit.edu>
Lucas Campbell <lxc7058@rit.edu>
Brendan Jones <bpj1051@rit.edu>
Gavriel Rachael-Homann<gxr2329@rit.edu>

----------------------------------------------------------

[File Locations]
All .txt files used by LBMS (including books.txt) are located in ./data/

----------------------------------------------------------

[Functional Limitations]
SearchRequests don't return results if all parameters are empty/*;
Books are purchased using their ISBN, rather than temporary ID based on earlier Searches.
Clean shutdowns only occur if the system is exited using the "quit;" request.
Statistics requests don't work for a range of days.
