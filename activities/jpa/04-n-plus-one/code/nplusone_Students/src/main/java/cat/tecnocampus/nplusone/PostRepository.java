package cat.tecnocampus.nplusone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // The naive read is the inherited findAll(): one select for the posts, then one
    // more select per post the first time its comments are touched (the N+1 problem).

    // TODO(student): make this load every post TOGETHER WITH its comments in a SINGLE
    // select, using a JPQL fetch join. Right now it runs a plain select, so the
    // comments are still fetched lazily — one extra query per post (N+1), and the test
    // fetch_join_loads_everything_in_one_statement stays red.
    // Hint: 'join fetch', and watch out for duplicate rows (use distinct, or a Set).
    @Query("select p from Post p")
    List<Post> findAllWithComments();

    // TODO(student): achieve the same single-select load using an @EntityGraph instead
    // of a fetch join. Add @EntityGraph(attributePaths = "comments") to this method so
    // 'comments' is fetched for THIS query only — not made globally eager on the mapping.
    // (You will need to import org.springframework.data.jpa.repository.EntityGraph.)
    @Query("select p from Post p")
    List<Post> findAllWithCommentsEntityGraph();
}
