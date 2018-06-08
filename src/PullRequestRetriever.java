import controller.PullRequestController;

import java.util.Scanner;

public class PullRequestRetriever {
    public static void main(String[] args) {

        PullRequestController prr = new PullRequestController();

        if(args.length == 0) {
            System.out.print("No repository is provided as an argument.\nPlease enter a repository ot get pull requests\n\t> ");
            Scanner in = new Scanner(System.in);
            prr.setRepository(in.next());
        }else
            prr.setRepository(args[0]);

        prr.printPRList();
    }
}
