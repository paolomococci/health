/**
 * This package contains all input DTOs (Data Transfer Object)s that the
 * application accepts from external clients (typically REST controllers). Each
 * record is immutable and represents the minimal data required to create or
 * update an entity (e.g. Article, Researcher, Reviewer, Review). Using records
 * keeps the code concise while still providing proper equals, hashCode
 * and toString implementations, and it makes the intent of each
 * payload crystal-clear.
 * 
 * Because these classes are used only for data transfer, they are kept
 * in a dedicated package to separate them from the core domain models.
 */
package local.health.research.inputs;
