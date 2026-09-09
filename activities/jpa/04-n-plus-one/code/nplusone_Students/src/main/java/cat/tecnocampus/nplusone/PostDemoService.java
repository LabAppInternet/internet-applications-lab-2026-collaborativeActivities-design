package cat.tecnocampus.nplusone;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

/**
 * Seeds a small dataset and runs the same read three ways, printing how many SQL
 * statements each one costs. The count comes from Hibernate's own {@link Statistics},
 * so it is not a manual tally of the log.
 */
@Service
public class PostDemoService {

    private final PostRepository postRepository;
    private final Statistics statistics;

    public PostDemoService(PostRepository postRepository, EntityManagerFactory entityManagerFactory) {
        this.postRepository = postRepository;
        this.statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        this.statistics.setStatisticsEnabled(true);
    }

    @Transactional
    public void seed(int posts, int commentsPerPost) {
        for (int p = 0; p < posts; p++) {
            Post post = new Post("Post " + p);
            for (int c = 0; c < commentsPerPost; c++) {
                post.addComment(new Comment("Comment " + c + " on post " + p));
            }
            postRepository.save(post);
        }
    }

    @Transactional
    public void showNaive() {
        report("naive findAll()", postRepository::findAll);
    }

    @Transactional
    public void showFetchJoin() {
        report("findAllWithComments() [fetch join]", postRepository::findAllWithComments);
    }

    @Transactional
    public void showEntityGraph() {
        report("findAllWithCommentsEntityGraph() [@EntityGraph]", postRepository::findAllWithCommentsEntityGraph);
    }

    private void report(String label, Supplier<List<Post>> read) {
        statistics.clear();
        List<Post> posts = read.get();
        // Touch every collection: if it was not fetched up front, this is where the
        // extra per-post selects fire.
        int comments = posts.stream().mapToInt(post -> post.getComments().size()).sum();
        long statements = statistics.getPrepareStatementCount();
        System.out.printf("[%-46s] %2d posts, %3d comments, %2d SQL statement(s)%n",
                label, posts.size(), comments, statements);
    }
}
