'use strict';

describe('Service: auctionService', function () {

  // load the service's module
  beforeEach(module('simpleAuctionFrontendApp'));

  // instantiate service
  var auctionService;
  beforeEach(inject(function (_auctionService_) {
    auctionService = _auctionService_;
  }));

  it('should do something', function () {
    expect(!!auctionService).toBe(true);
  });

});
