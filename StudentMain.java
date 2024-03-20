import java.io.*;

class Student {
    static int PRNCount;
    private int PRN;
    String Branch;
    int roll;
    String name;
    double percentage;

    Student(String Branch, int roll, String name, double percentage) {
        this.PRN = PRNCount;
        this.Branch = Branch;
        this.roll = roll;
        this.name = name;
        this.percentage = percentage;

    }

    int getPRN() {

        return this.PRN;

    }

    void SetPRN(int prn) {
        this.PRN = prn;
    }

}

class Operations {

    Student objectify(String content) {
        // System.out.println(content);
        String arr[] = content.split("\t   ");
        Student obj = new Student(arr[1], Integer.parseInt(arr[2]), arr[3], Double.parseDouble(arr[4]));
        obj.SetPRN(Integer.parseInt(arr[0]));
        return obj;
    }

    String getData(Student obj) {
        return (obj.getPRN() + "\t   " + obj.Branch + "\t   " + obj.roll + "\t   "
                + obj.name + "\t   " + obj.percentage);
    }

    void display(Student obj) {

        System.out.println("PRN: " + obj.getPRN() + "\t Branch: " + obj.Branch + "\t Roll: " + obj.roll + "\t Name: "
                + obj.name + "\t Percentage: " + obj.percentage);

    }

    boolean update(Student obj, String decide) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            boolean confirm;
            switch (decide) {

                case "branch":
                    System.out.print("Enter the updated value: ");
                    String branch = br.readLine();
                    System.out.println(
                            "Are you sure you want to change " + obj.Branch + " with " + branch + "\nYes \tNo");

                    confirm = br.readLine().equalsIgnoreCase("yes");

                    if (confirm) {
                        obj.Branch = branch;
                        return true;
                    }

                    return false;

                case "roll no.":
                    System.out.print("Enter the updated value: ");
                    int roll = Integer.parseInt(br.readLine());
                    System.out.println("Are you sure you want to change " + obj.roll + " with " + roll + "\nYes \tNo");

                    confirm = br.readLine().equalsIgnoreCase("yes");

                    if (confirm) {
                        obj.roll = roll;
                        return true;
                    }

                    return false;

                case "name":
                    System.out.print("Enter the updated value: ");
                    String name = br.readLine();
                    System.out.println("Are you sure you want to change " + obj.name + " with " + name + "\nYes \tNo");

                    confirm = br.readLine().equalsIgnoreCase("yes");

                    if (confirm) {
                        obj.name = name;
                        return true;
                    }

                    return false;

                case "percentage":
                    System.out.print("Enter the updated value: ");
                    double perc = Double.parseDouble(br.readLine());
                    System.out.println(
                            "Are you sure you want to change " + obj.percentage + " with " + perc + "\nYes \tNo");

                    confirm = br.readLine().equalsIgnoreCase("yes");

                    if (confirm) {
                        obj.percentage = perc;
                        return true;
                    }

                    return false;

                case "<":
                    return false;

                case "back":
                    return false;

                default:
                    System.out.println("Enter valid choice..");

            }

        } catch (IOException e) {
            System.out.println("IOException Caught");

        }
        return false;

    }

    Student[] Sort(Student[] st, String decide) {
        Student temp;

        switch (decide) {

            case "PRN":

                for (int i = 0; i < Student.PRNCount; i++) {

                    for (int j = 0; j < Student.PRNCount - 1 - i; j++) {

                        if (st[j].getPRN() > st[j + 1].getPRN()) {
                            temp = st[j];
                            st[j] = st[j + 1];
                            st[j + 1] = temp;

                        }

                    }

                }

                break;

            case "alphabetically":

                for (int i = 0; i < Student.PRNCount; i++) {

                    for (int j = 0; j < Student.PRNCount - 1 - i; j++) {

                        String str1 = st[j].name.toUpperCase();
                        String str2 = st[j + 1].name.toUpperCase();

                        if (str1.compareTo(str2) > 0) {
                            temp = st[j];
                            st[j] = st[j + 1];
                            st[j + 1] = temp;

                        }

                    }

                }

                break;

            case "percentage wise":

                for (int i = 0; i < Student.PRNCount; i++) {

                    for (int j = 0; j < Student.PRNCount - 1 - i; j++) {

                        if (st[j].percentage < st[j + 1].percentage) {
                            temp = st[j];
                            st[j] = st[j + 1];
                            st[j + 1] = temp;

                        }

                    }

                }

                break;

            default:
                System.out.println("Enter valid choice..");
                return null;

        }

        return st;
    }

    void saveData(File f, Student[] st) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false), true);
            String head = "PRN\t Branch\t Roll No.\t Name\t Percentage";
            pw.println(head);

            for (int i = 0; i < Student.PRNCount; i++) {

                pw.println(getData(st[i]));

            }
        } catch (IOException e) {
            System.out.println("IOException Caught");
        }
    }

}

class AsThread extends Thread {
    Student[] st;
    Operations op;
    File f;
  static long lastEdited = 0;
  static  long lastSaved = 0;
  static boolean isThreadAsleep = false;

    AsThread(Student[] st, Operations op, File f) {
        this.st = st;
        this.op = op;
        this.f = f;
    }

    public void run() {

        while (true) {

           
            //System.out.println("Last Edited "+ lastEdited);
            if (lastEdited != 0) {
                
                if (System.currentTimeMillis() >= lastEdited + 10000) {
                    synchronized (op) {
                        op.saveData(f, st);
                       // System.out.println("Inside Sync Block");
                        lastSaved = System.currentTimeMillis();
                          try{
                  isThreadAsleep = true;      
                this.wait();
                        
                        }
                        catch(InterruptedException e)
                        {
                            System.out.println("Interrupted Exception caught");
                        }
                       

                    }

                    

                }

            }

        }

    }

}

class StudentMain {

    public static void main(String args[]) {

        String choice;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int roll, count = 0, readCount = 0;
        String branch, name;
        double perc;

        Student st[] = new Student[100];
        Operations op = new Operations();

        try {

            do {

                File f = new File("./Students.txt");

                if (f.exists()) {

                    if (readCount == 0) {

                        FileInputStream fis = new FileInputStream(f);
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));

                        String record = "";

                        while ((record = bfr.readLine()) != null) {

                            if (record.charAt(0) != 'P') {

                                st[count] = op.objectify(record);
                                System.out.println("You here");
                                System.out.println(st[count]);
                                /*
                                 * System.out.println(count);
                                 * 
                                 * 
                                 * System.out.println(st[count].getPRN());
                                 * 
                                 */

                                if (Student.PRNCount < st[count].getPRN())
                                    Student.PRNCount = st[count].getPRN();

                                count++;
                                readCount++;

                            }

                        }

                        bfr.close();
                        fis.close();

                    }

                }

                AsThread t1 = new AsThread(st, op, f);
                t1.start();

                System.out.println("-----MENU------");
                System.out.println(
                        "Select the operations to be performed\n\"Add\" Student\n\"Update\" Student Data\n\"Delete\" Student\n\"Sort\" the Data\n\"Display\" \n\"Exit.\n\"Save\"\"\nEnter your choice: ");

                choice = br.readLine().toLowerCase();

                switch (choice) {
                    case "add":
                        if (!(Student.PRNCount == st.length)) {

                            System.out.print("Enter Student's Branch: ");
                            branch = br.readLine();

                            System.out.println("Enter Student's Roll no.: ");
                            roll = Integer.parseInt(br.readLine());

                            System.out.print("Enter Student's Name: ");
                            name = br.readLine();

                            System.out.print("Enter Student's percentage: ");
                            perc = Double.parseDouble(br.readLine());

                            st[Student.PRNCount++] = new Student(branch, roll, name, perc);

                            AsThread.lastEdited = System.currentTimeMillis();
                           // System.out.println("Last edited "+ t1.lastEdited);
                           if(AsThread.isThreadAsleep)
                           t1.notify();

                        }

                        else
                            System.out.println("Storage full.Can't add.");

                        break;

                    case "update":
                        if (Student.PRNCount != 0) {
                            System.out.print("Enter the PRN No. of the Student to be updated:  ");
                            int prn = Integer.parseInt(br.readLine());

                            for (int i = 0; i < Student.PRNCount; i++) {

                                if (prn == st[i].getPRN()) {

                                    System.out.println("Here's the data of PRN: " + prn);
                                    op.display(st[i]);

                                    String decide;
                                    do {
                                        System.out.println(
                                                "What value you Want to update\n\"Branch\"\t \"Roll No.\"\t \"Name\"\t \"Percentage\"\t \"Back\"");

                                        decide = br.readLine().toLowerCase();

                                        if (op.update(st[i], decide))
                                            System.out.println("Updated Successfully.");

                                        else

                                            System.out.println("Something happened.Couldn't update");

                                    } while (decide.equalsIgnoreCase("back") || decide.equalsIgnoreCase("<"));

                                } else
                                    System.out.println("Student not found!!");

                            }

                        }
                        break;

                    case "delete":
                        if (Student.PRNCount != 0) {
                            System.out.print("Enter the PRN No. of the Student to be deleted:  ");
                            int prn = Integer.parseInt(br.readLine());

                            for (int i = 0; i < Student.PRNCount; i++) {

                                if (prn == st[i].getPRN()) {

                                    System.out.println("Here's the data of PRN: " + prn);
                                    op.display(st[i]);

                                    System.out.println("Do you want to delete " + "(" + st[i].getPRN() + ")"
                                            + st[i].name + "\nYes \t No");

                                    if (br.readLine().equalsIgnoreCase("yes")) {
                                        Student.PRNCount--;
                                        System.out.println("Deleted Successfully.!!");
                                    }

                                    else
                                        System.out.println("Couldn't Delete.");

                                }

                            }
                        } else
                            System.out.println("Already Empty..");
                        break;

                    case "sort":

                        System.out.println("Sort on the basis of \n PRN \t Branch \t Alphabetically \tPercentage Wise");

                        String decide = br.readLine();

                        Student temp[] = op.Sort(st, decide);
                        if (temp != null) {
                            System.out.println("Sorted Successfully.");
                            st = temp;
                        } else
                            System.out.println("Couldnt sort");
                        break;

                    case "display":

                        if (Student.PRNCount != 0) {
                            System.out.println("Here's the data: ");

                            for (int i = 0; i < Student.PRNCount; i++) {

                                op.display(st[i]);

                            }
                        }
                        break;

                    case "save":

                        op.saveData(f, st);

                        /*
                         * PrintWriter pw = new PrintWriter(new FileOutputStream(f, false), true);
                         * String head = "PRN\t Branch\t Roll No.\t Name\t Percentage";
                         * pw.println(head);
                         * 
                         * for (int i = 0; i < Student.PRNCount; i++) {
                         * 
                         * pw.println(op.getData(st[i]));
                         * 
                         * }
                         */

                        readCount = 0;

                    default:
                        System.out.println("Enter Valid choice..");

                }

            }

            while (!choice.equalsIgnoreCase("exit"));

        } catch (IOException e) {
            System.out.println("IOException caught.");
        }
    }

}