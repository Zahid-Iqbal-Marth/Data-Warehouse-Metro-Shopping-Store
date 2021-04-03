Steps

0 - Run the “metro_db” sql script provide my Instructor to create DB and then open mysql        
     Workbench then right click on “metro_db” database on the side bar and select default db.

1 - Unzip the submitted file.

2 - Run the SQL file ‘create_DW’ using mysql workbench.

3 - Unzip the Java project named "hyperJoin" in the Eclipse working directory

4 -Start Eclipse and Open the project ‘hybridJoin’ in eclipse.

5 - Open the file ‘Staging_Area.java’.

6 - username as pass is set to “root”, you can change that on line number 172 in       
     ‘Staging_Area.java’ file.

7 - I have used 
     import org.apache.commons.collections4.MultiValuedMap;
     import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
     Maps in project and added their files as External jars in project.
     If it still gives an error that add the provided jar files.

8 -Now  run   ‘Staging_Area.java’ file.

9 - Wait for 5-10 mins until all the data loads into the DWH.

10 - Open the sql file  ‘proj-queries' in mysql workbench to run all the queries.