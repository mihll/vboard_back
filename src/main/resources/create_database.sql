create database db_vboard; -- Creates the new database
create user 'springuser'@'%' identified by 'j248Y2d3qW6xWYVi4DRUEZun686JvgDA'; -- Creates new user for use with spring application
grant all on db_vboard.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database