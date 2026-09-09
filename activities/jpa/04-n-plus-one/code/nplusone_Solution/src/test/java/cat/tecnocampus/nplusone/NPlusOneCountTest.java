package cat.tecnocampus.nplusone;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Counts the SQL statements each read costs, using Hibernate's {@link Statistics}.
 * The naive read pays 1 + N; a fetch join and an entity graph each pay 1.
 */
@DataJpaTest
class NPlusOneCountTest {

    private static final int POSTS = 10;
    private static final int COMMENTS_PER_POST = 3;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics statistics;

    @BeforeEach
    void seed() {
        statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);
        for (int p = 0; p < POSTS; p++) {
            Post post = new Post("Post " + p);
            for (int c = 0; c < COMMENTS_PER_POST; c++) {
                post.addComment(new Comment("Comment " + c));
            }
            entityManager.persist(post);
        }
    }

    /** Start each read from a cold persistence context and count only its statements. */
    private long statementsFor(Supplier<List<Post>> read) {
        entityManager.flush();
        entityManager.clear();
        statistics.clear();
        List<Post> posts = read.get();
        posts.forEach(post -> post.getComments().size()); // force the lazy load, if any
        return statistics.getPrepareStatementCount();
    }

    @Test
    void naive_findAll_causes_N_plus_one() {
        assertThat(statementsFor(() -> postRepository.findAll()))
                .isEqualTo(POSTS + 1); // 1 for the posts, then 1 per post for its comments
    }

    @Test
    void fetch_join_loads_everything_in_one_statement() {
        assertThat(statementsFor(postRepository::findAllWithComments))
                .isEqualTo(1);
    }

    @Test
    void entity_graph_loads_everything_in_one_statement() {
        assertThat(statementsFor(postRepository::findAllWithCommentsEntityGraph))
                .isEqualTo(1);
    }
}
