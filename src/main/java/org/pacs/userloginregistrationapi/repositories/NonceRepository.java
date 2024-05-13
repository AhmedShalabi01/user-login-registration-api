package org.pacs.userloginregistrationapi.repositories;

import org.pacs.userloginregistrationapi.documents.NonceTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NonceRepository extends MongoRepository<NonceTracker, String> {
}
