package student;
/**
 *  Copyright (c)  2016 Ludovic Taffin
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 import java.io.File;
 import java.io.IOException;
 import java.io.PrintWriter;
public class Runner {
	// Code verificateur
	public static void main(String[] args) {

		try{
			int nbsteps=0;
			boolean ned = false;
			//On commence avec le code de test de l'étudiant
			//Running du code
			int level =1;
			Spies s1 =null;
			while(level<=14){
				s1 = new Spies();
				switch (level) {
            case 1:  s1.contest01();
                     break;
						case 2:  s1.contest02();
										 break;
						case 3:  s1.contest03();
										break;
						case 4:  s1.contest04();
										break;
						case 5:  s1.contest05();
										break;
						case 6:  s1.contest06();
										break;
						case 7:  s1.contest07();
										break;
						case 8:  s1.contest08();
										break;
						case 9:  s1.contest09();
										break;
						case 10:  s1.contest10();
										break;
						case 11:  s1.contest11();
										break;
						case 12:  s1.contest12();
										break;
						case 13:  s1.contest13();
										break;
            case 14: s1.contest14();
                     break;
						default: throw new Exception();
        }
				try{
						s1.behave();
						ned = s1.getFoundNed();
						if (ned){
							level++;
						}else{
							System.out.println("L'espion a failli à sa tâche.");
              if(level ==1){
                nbsteps += s1.getSteps();
              }
							printGrade("printresult.txt", ""+nbsteps,""+level);
							System.exit(-1);
						}
            nbsteps += s1.getSteps();
				}catch(SpiesAreDeadException se){
					System.out.println("L'espion est mort.");
					printGrade("printresult.txt", ""+nbsteps,""+level);
					System.exit(-1);
				}
			}
			//TOTAL SUCCEED
			printGrade("printresult.txt", ""+nbsteps,""+level);
			System.exit(127);
		}catch(Exception e){
			System.out.println("Quelque chose s'est mal passé:"+e.toString());
		}
	}
	static void printGrade(String filename, String grade, String level) {

        try {
            PrintWriter pw = new PrintWriter("student/" + filename);
            pw.println(grade);
						pw.println(level);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            System.err.println("Couldn't write grade file with exception : " + e.toString() + e.getMessage());
        }
  }
}
