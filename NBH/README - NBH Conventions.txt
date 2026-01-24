
URL-Design                                          Methoden-Namen                  Return Page-Namen
==========                                          ==============                  =================

GET     /users              → Userliste anzeigen    listAllUsers() oder
                                                    listAllUsersPaginated()         users/list-users

GET     /users/{id}         → Details anzeigen      editUser()                      users/detail-user
POST    /users/{id}         → Update                updateUser()                    return saveMember()

GET     /users/search       → Suche                 searchUsers()                   redirect:/members

GET     /users/new          → Leeres Formular       newUser()                       users/detail-user
PO      /accountables/new-accountable	            newAccountable()	            accountables/detail-accountable

POST    /users              → Speichern             saveUser()                      ERROR: users/detail-user
                                                                                    OK: redirect:/users/ + user.getId()

        /accountables/accountable	            	saveAccountable()	            ERROR: accountables/detail-accountable
                                                                                    OK: redirect: zu der accountable Seite

GET     /user/sort/{sort}   → Sortieren             listAllUsers(sort)              users/list-users

POST    /users/delete/{id}  → Löschen               deleteUser()                    redirect:/users

👉 Nur GET & POST
👉 Semantisch sauber für MVC
👉 Refresh-Sicher


Offene Punkte
=============

- Unterscheidung edit/view - wann darf man bearbeiten, wann nur anzeigen? Und wie bilden wir das ab?

