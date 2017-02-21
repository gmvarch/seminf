package com.gmv.techhip.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gmv.techhip.config.JHipsterProperties;
import com.gmv.techhip.domain.Vote;
import com.gmv.techhip.domain.VoteAgg;
import com.gmv.techhip.repository.VoteRepository;
import com.gmv.techhip.web.rest.util.HeaderUtil;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Vote.
 */
@RestController
@RequestMapping("/api")
public class VoteResource {

	private final Logger log = LoggerFactory.getLogger(VoteResource.class);

	@Inject
	private VoteRepository voteRepository;

	@Inject
	private JHipsterProperties redisProperties;

	@Inject
    OptionClient optionClient;
	
	/**
	 * POST /votes : Create a new vote.
	 *
	 * @param vote
	 *            the vote to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new vote, or with status 400 (Bad Request) if the vote has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/votes")
	@Timed
	public ResponseEntity<Vote> createVote(@RequestBody Vote vote, HttpServletRequest request) throws URISyntaxException {
		vote.setSource(request.getSession().getId());
		
		log.debug("REST request to save Vote : {}", vote);
		if (vote.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("vote", "idexists", "A new vote cannot already have an ID"))
					.body(null);
		}
		Vote result = voteRepository.save(vote);

		String connectionString = "redis://" + redisProperties.getRedis().getUsername() + "@" + redisProperties.getRedis().getHost() + ":" + redisProperties.getRedis().getPort();
		System.out.println("######### " + connectionString);
		
		RedisClient redisClient = new RedisClient(RedisURI.create("redis://votedata@redis-14584.c11.us-east-1-3.ec2.cloud.redislabs.com:14584"));
		RedisConnection<String, String> connection = redisClient.connect();
		connection.incr(vote.getValue());
		connection.close();
        redisClient.shutdown();
        
		return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("vote", result.getId().toString())).body(result);
	}

	/**
	 * PUT /votes : Updates an existing vote.
	 *
	 * @param vote
	 *            the vote to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         vote, or with status 400 (Bad Request) if the vote is not valid,
	 *         or with status 500 (Internal Server Error) if the vote couldnt be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/votes")
	@Timed
	public ResponseEntity<Vote> updateVote(@RequestBody Vote vote) throws URISyntaxException {
		log.debug("REST request to update Vote : {}", vote);
		if (vote.getId() == null) {
			return createVote(vote, null);
		}
		Vote result = voteRepository.save(vote);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("vote", vote.getId().toString()))
				.body(result);
	}

	/**
	 * GET /votes : get all the votes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of votes in
	 *         body
	 */
	@GetMapping("/votes")
	@Timed
	public List<Vote> getAllVotes() {
		log.debug("REST request to get all Votes");
//		List<Vote> votes = new ArrayList<Vote>();
//		
//		RedisClient redisClient = new RedisClient(RedisURI.create("redis://votedata@redis-14584.c11.us-east-1-3.ec2.cloud.redislabs.com:14584"));
//		RedisConnection<String, String> connection = redisClient.connect();
//		List<String> llaves = connection.keys("*");
//		Long i=0l;
//		for(String llave:llaves){
//			Vote aux = new Vote();
//			aux.setSource(llave);
//			aux.setValue(connection.get(llave));
//			aux.setId(i++);
//			votes.add(aux);
//		}
//		connection.close();
//        redisClient.shutdown();
		
        
		return voteRepository.findAll();
	}

	/**
	 * GET /votes/:id : get the "id" vote.
	 *
	 * @param id
	 *            the id of the vote to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the vote,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/votes/{id}")
	@Timed
	public ResponseEntity<Vote> getVote(@PathVariable Long id) {
		log.debug("REST request to get Vote : {}", id);
		Vote vote = voteRepository.findOne(id);
		return Optional.ofNullable(vote).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /votes/:id : delete the "id" vote.
	 *
	 * @param id
	 *            the id of the vote to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/votes/{id}")
	@Timed
	public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
		log.debug("REST request to delete Vote : {}", id);
		voteRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vote", id.toString())).build();
	}

	/**
	 * GET /votes/:id : get the "id" vote.
	 *
	 * @param id
	 *            the id of the vote to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the vote,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/votesagg")
	@Timed
	public List<VoteAgg> getVoteAgg() {
		log.debug("REST request votesagg");
		//TODO

		List<Option> opciones = (List<Option>) optionClient.findAll();
	
		RedisClient redisClient = new RedisClient(RedisURI.create("redis://votedata@redis-14584.c11.us-east-1-3.ec2.cloud.redislabs.com:14584"));
		RedisConnection<String, String> connection = redisClient.connect();
		List<VoteAgg> retorno = new ArrayList<VoteAgg>();
		for(Option opt:opciones)
		{
			VoteAgg aux = new VoteAgg();
			aux.setOption(opt.getOption());			
			aux.setValue(connection.get(opt.getOption()));
			retorno.add(aux);
		}
		connection.close();
        redisClient.shutdown();
		return retorno;
	}
	
}
