package local.health.research.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import local.health.research.models.Article;

/**
 * ArticleRepository is a Spring Data interface that provides
 * CRUD operations for the Article entity. By extending
 * JpaRepository<Article, Long> it inherits methods such as
 * save, findById, findAll, delete, etc. The generic
 * parameters specify that the entity type is Article and
 * its primary key type is Long.
 *
 * The @Repository annotation marks this interface as a
 * Spring bean of type Repository. It also enables exception
 * translation for database operations, turning JPA
 * exceptions into Spring's DataAccessException hierarchy.
 *
 * No additional query methods are defined here, but new methods
 * can be added following Spring Data naming conventions.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

        /**
         * Removes all researcher-article link rows that refer to the
         * given article. The deletion is executed via a native SQL
         * statement because the association table does not have
         * an explicit entity mapping.
         *
         * @param articleId the id of the article whose associations
         *                  should be removed
         *
         * @Modifying indicates that this query modifies data
         *            (it is a DELETE operation).
         *
         * @Query executes the following SQL:
         *        "DELETE FROM RESEARCHER_ARTICLE RA WHERE RA.ARTICLE_ID = :articleId"
         */
        @Modifying
        @Query(value = "DELETE FROM RESEARCHER_ARTICLE RA WHERE RA.ARTICLE_ID = :articleId", nativeQuery = true)
        void deleteResearcherAssociationsByArticleId(@Param("articleId") Long articleId);

        /**
         * Removes all reviewer-article link rows that refer to the
         * specified article. It uses a native SQL delete because the
         * association table is not represented by a JPA entity.
         *
         * @param articleId the id of the article whose reviewer links
         *                  should be deleted
         *
         * @Modifying signals that the operation changes data.
         *
         * @Query performs:
         *        "DELETE FROM REVIEWER_ARTICLE RA WHERE RA.article_id = :articleId"
         */
        @Modifying
        @Query(value = "DELETE FROM REVIEWER_ARTICLE RA WHERE RA.article_id = :articleId", nativeQuery = true)
        void deleteReviewerAssociationsByArticleId(@Param("articleId") Long articleId);

        /**
         * Inserts a new link between a researcher and an article
         * into the RESEARCHER_ARTICLE join table. Because the join
         * table is not mapped as a JPA entity, a native INSERT
         * statement is used.
         *
         * @param articleId    the id of the article
         * @param researcherId the id of the researcher to associate
         *
         * @Modifying indicates that this query writes to the database.
         *
         * @Query executes:
         *        "INSERT INTO RESEARCHER_ARTICLE (ARTICLE_ID, RESEARCHER_ID) VALUES
         *        (:articleId, :researcherId)"
         */
        @Modifying
        @Query(value = "INSERT INTO RESEARCHER_ARTICLE (ARTICLE_ID, RESEARCHER_ID) VALUES (:articleId, :researcherId)", nativeQuery = true)
        void addResearcherAssociation(@Param("articleId") Long articleId,
                        @Param("researcherId") Long researcherId);

        /**
         * Inserts a new link between a reviewer and an article
         * into the REVIEWER_ARTICLE join table. This method is also
         * executed via a native INSERT because the association table
         * is not represented by a JPA entity.
         *
         * @param articleId  the id of the article
         * @param reviewerId the id of the reviewer to associate
         *
         * @Modifying marks the query as modifying data.
         *
         * @Query performs:
         *        "INSERT INTO REVIEWER_ARTICLE (article_id, reviewer_id) VALUES
         *        (:articleId, :reviewerId)"
         */
        @Modifying
        @Query(value = "INSERT INTO REVIEWER_ARTICLE (article_id, reviewer_id) VALUES (:articleId, :reviewerId)", nativeQuery = true)
        void addReviewerAssociation(@Param("articleId") Long articleId,
                        @Param("reviewerId") Long reviewerId);
}
