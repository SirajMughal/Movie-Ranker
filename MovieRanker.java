
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;


public class MovieRanker {

	public static void main(String[] args) {
		
		File file = new File("/ratings.txt");

		ArrayList<MovieRating> rl = new ArrayList<MovieRating>();
		
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
                String[] tkns = line.split("\\t"); // tabs separate tokens
                MovieRating nr = new MovieRating(tkns[0], "-" + tkns[1], tkns[2]);
				rl.add(nr);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int minVotes = 1;
		int numRecords = 1;
		Scanner input = new Scanner(System.in);
		
		while (true) {
			MaxHeap<MovieRating> min = new MaxHeap<MovieRating>();
			MaxHeap<MovieRating> max = new MaxHeap<MovieRating>();
			float maxVal = 0;
			int counter = 0;
			System.out.println();
			System.out.println("Enter minimum vote threshold and number of records:");
			minVotes = input.nextInt();
			numRecords = input.nextInt();
			
			if (minVotes * numRecords == 0)
				break;
			long startTime = System.currentTimeMillis();
			
			for (MovieRating mr : rl) {
				if (mr.getVotes() >= minVotes) {
					
					if (counter < numRecords) {
						min.insert(mr);
						counter += 1;
						if (mr.getRating() > maxVal)
							maxVal = mr.getRating();
					}	
					else {
						if (mr.getRating() <= maxVal) {
							min.removemax();
							min.insert(mr);
							MovieRating m = min.removemax();
							maxVal = m.getRating();
							min.insert(m);
						}
					}	
				}
			}
			
			while (! min.isEmpty()) {
				MovieRating temp = min.removemax();
				MovieRating m1 = new MovieRating(temp.getVotes() + "", - temp.getRating() + "", temp.getTitle());
				max.insert(m1);
			}
			while (! max.isEmpty()) {
				System.out.println(max.removemax());
			}
		
			
            System.out.println();;
			long readTime = System.currentTimeMillis();
			System.out.println("Time: " + (readTime - startTime) +" ms");
		}
	}
}
