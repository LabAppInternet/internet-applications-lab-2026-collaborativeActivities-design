package cat.tecnocampus.nplusone;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // The naive read is the inherited findAll(): one select for the posts, then one
    // more select per post the first time its comments are touched (the N+1 problem).

    /** One select that loads every post together with its comments (JPQL fetch join). */
    @Query("select distinct p from Post p join fetch p.comments")
    List<Post> findAllWithComments();

    /** Same single-select load, expressed with an entity graph instead of a fetch join. */
    @EntityGraph(attributePaths = "comments")
    @Query("select p from Post p")
    List<Post> findAllWithCommentsEntityGraph();
}
