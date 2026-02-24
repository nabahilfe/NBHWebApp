## Required Data

Documentation of the data that is generated or required for this project 

### User

#### A standard System Administrator is generated at startup with the following data

- Name: System Administrator
- email: webmaster@nabahilfe.eu

Set your password by registering with the email address above. 
This user has all permissions and is used for administration of the system. 

This initial setup generates also the required Administrator role and assigns it to the user.

Both entities, the user and the role, are not editable nor deletable.


## E-Mail setup in Hetzner konsoleH

**webmaster@nabahilfe.eu** is the email address that is used for System Administrator.

- Mails are forwarded (and then deleted) as configured in the hetzner webmail app, currently this is
  - admin@weissboeck.info
  - at least a second admin must be set
 
**info@nabahilfe.eu** is the email address that is used for sending emails from the system. 

- This email address is used for all outgoing emails, such as registration, notifications, etc.
- All incoming mails are silently discarded, so please do not send any emails to this address.

**kontakt@nabahilfe.eu** is the email address that is published on the web page and is used for receiving emails from users.

- incoming emails to this address are forwarded to email addresses as configured in hetzner Webmail of this account.
- all incoming emails are deleted after forwarding to the configured accounts.
- current forwarding configuration is as follows:
  - mweissboeck@gmail.com

**test@nabhilfe.eu** only for testing


